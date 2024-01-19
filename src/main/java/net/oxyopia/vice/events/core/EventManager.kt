package net.oxyopia.vice.events.core

import net.oxyopia.vice.events.ViceEvent
import net.oxyopia.vice.utils.DevUtils
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * EventManager class for managing event subscriptions and handling event hooks.
 * Uses a ConcurrentHashMap to store the subscribers for thread-safe access.
 * Each event class is mapped to a list of DefaultListeners.
 *
 * @author pvpb0t
 * @author Oxyopiia
 */
class EventManager {
	data class EventListener(var event: Class<out ViceEvent?>, var target: Method, var source: Any? = null)

	/**
	 * The subscribers map holds a list of listeners for each event type.
	 * The ConcurrentHashMap class is used to ensure thread-safety.
	 */
	val subscribers: ConcurrentHashMap<Class<out ViceEvent>, ArrayList<EventListener>> = ConcurrentHashMap()

	/**
	 * Subscribes an object to events that it has specified methods for.
	 * This method scans the object's class and its superclasses for methods that are annotated with @EventHook.
	 * For each such method, it creates a DefaultListener object with the method and the event class that it handles.
	 * The listener is added to the subscribers map for the corresponding event class.
	 * If the event class has any inner classes that extend it, the method is also added as a listener for those classes.
	 *
	 * @param obj the object to be subscribed to events.
	 */
	fun subscribe(obj: Any) {
		var clazz: Class<*>? = obj.javaClass
		while (clazz != null) {
			for (method in clazz.declaredMethods) {
				if (method.isAnnotationPresent(SubscribeEvent::class.java)) {
					val parameterTypes = method.parameterTypes

					if (parameterTypes.isNotEmpty() && (ViceEvent::class.java.isAssignableFrom(parameterTypes[0]))) {
						val eventClazz = parameterTypes[0]

						if (ViceEvent::class.java.isAssignableFrom(eventClazz)) {
							val safeEventClazz = eventClazz.asSubclass(ViceEvent::class.java)

							val listener = EventListener(safeEventClazz, method)
							listener.source = obj

							subscribers.computeIfAbsent(safeEventClazz) { ArrayList() }.add(listener)

							for (subClazz in eventClazz.declaredClasses) {
								if (eventClazz.isAssignableFrom(subClazz) && ViceEvent::class.java.isAssignableFrom(subClazz)) {
									val safeSubClazz = subClazz.asSubclass(ViceEvent::class.java)
									val subListener = EventListener(safeSubClazz, method)
									subListener.source = obj
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
	 * This method retrieves the event class and iterates over its class hierarchy.
	 * For each class, it checks if the subscribers map contains any listeners for that class.
	 * If so, it creates a copy of the list of listeners and iterates over it.
	 * For each listener, it uses Java's MethodHandles API to invoke the method that handles the event.
	 * If an exception is thrown during the invocation, it is caught and printed to the standard error stream.
	 *
	 * @param event the event to be hooked to listeners.
	 */
	fun publish(event: ViceEvent): ViceEvent {
		var clazz: Class<*>? = event.javaClass

		while (clazz != null) {
			val isCancelable = ViceEvent.Cancelable::class.java.isAssignableFrom(clazz)

			if (subscribers.containsKey(clazz)) {
				val listenersCopy = subscribers[clazz]?.let { ArrayList(it) } ?: return event
				for (listener in listenersCopy) {
					try {
						val lookup = MethodHandles.lookup()
						val handle = lookup.unreflect(listener.target)
						handle.invoke(listener.source, event)
					} catch (e: Throwable) {
						DevUtils.sendErrorMessage(e, "An error occurred invoking a " + clazz.simpleName)
					}
				}
			}

			clazz = clazz.superclass
		}

		return event
	}


	/**
	 * Unsubscribes the given object from all events it was previously subscribed to.
	 * This method removes all listeners that have the given object as their source from the subscribers map.
	 * It also removes any event classes that no longer have any listeners.
	 * If an event class has any inner classes that extend it, it also removes any listeners for those classes.
	 *
	 * @param object the object to unsubscribe
	 */
	fun unsubscribe(`object`: Any) {
		subscribers.values.forEach(Consumer { listeners: ArrayList<EventListener>? -> listeners!!.removeIf { listener: EventListener -> listener.source === `object` } })
		subscribers.entries.removeIf { entry: Map.Entry<Class<out ViceEvent>, ArrayList<EventListener>?> -> entry.value!!.isEmpty() }
		subscribers.keys.stream()
			.filter { clazz: Class<out ViceEvent> -> clazz.declaredClasses.isNotEmpty() }
			.flatMap { clazz: Class<out ViceEvent> -> Arrays.stream(clazz.declaredClasses) }
			.filter { subClazz: Class<*> -> subClazz.isAssignableFrom(subClazz.declaringClass) }
			.forEach { subClazz: Class<*>? ->
				val subListeners = subscribers[subClazz]
				if (subListeners != null) {
					subListeners.removeIf(Predicate { listener: EventListener -> listener.source === `object` })
					if (subListeners.isEmpty()) {
						subscribers.remove(subClazz)
					}
				}
			}
	}
}