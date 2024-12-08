package net.oxyopia.vice.events.core

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ViceEvent
import net.oxyopia.vice.utils.DevUtils
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

class EventManager {
	data class EventListener(var event: Class<out ViceEvent?>, var target: Method, var source: Any)

	val subscribers: ConcurrentHashMap<Class<out ViceEvent>, ArrayList<EventListener>> = ConcurrentHashMap()

	/**
	 * Subscribes any methods marked with @SubscribeEvent to the Event System.
	 *
	 * @param obj the object to be subscribed to events.
	 */
	fun subscribe(obj: Any) {
		var clazz: Class<*>? = obj.javaClass
		while (clazz != null) {
			for (method in clazz.declaredMethods) {
				if (method.isAnnotationPresent(SubscribeEvent::class.java)) {
					val parameterTypes = method.parameterTypes

					if (parameterTypes.isNotEmpty() && ViceEvent::class.java.isAssignableFrom(parameterTypes[0])) {
						val eventClazz = parameterTypes[0]

						if (ViceEvent::class.java.isAssignableFrom(eventClazz)) {
							val safeEventClazz = eventClazz.asSubclass(ViceEvent::class.java)

							val listener = EventListener(safeEventClazz, method, obj)
							subscribers.computeIfAbsent(safeEventClazz) { ArrayList() }.add(listener)

							for (subClazz in eventClazz.declaredClasses) {
								if (eventClazz.isAssignableFrom(subClazz) && ViceEvent::class.java.isAssignableFrom(subClazz)) {
									val safeSubClazz = subClazz.asSubclass(ViceEvent::class.java)
									val subListener = EventListener(safeSubClazz, method, obj)
									subscribers.computeIfAbsent(safeSubClazz) { ArrayList() }.add(subListener)
								}
							}
						}
					}
				}
			}
			clazz = clazz.superclass
		}
	}

	/**
	 * Hooks an event to all its subscribed listeners.
	 * If an exception is thrown during invocation, it is caught and printed to the Minecraft Chat using a Vice Error.
	 */
	fun <T: ViceEvent> publish(event: T): T {
		if (DevUtils.hasDevMode(Vice.devConfig.THROTTLE_ALL_EVENTS)) return event

		var clazz: Class<*>? = event.javaClass

		while (clazz != null) {
			if (subscribers.containsKey(clazz)) {
				val listenersCopy = subscribers[clazz]?.let { ArrayList(it) } ?: return event
				for (listener in listenersCopy) {
					try {
						val lookup = MethodHandles.lookup()
						val handle = lookup.unreflect(listener.target)
						handle.invoke(listener.source, event)

						if (event is ViceEvent.Cancelable<*> && event.isCanceled) {
							break
						}

					} catch (e: Throwable) {
						DevUtils.sendErrorMessage(e, "An error occurred invoking a " + clazz.simpleName)
					}
				}
			}

			clazz = clazz.superclass
		}

		event.onceSent()
		return event
	}
}