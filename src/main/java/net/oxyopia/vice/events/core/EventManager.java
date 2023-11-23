package net.oxyopia.vice.events.core;

import net.oxyopia.vice.events.BaseEvent;
import net.oxyopia.vice.utils.DevUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EventManager class for managing event subscriptions and handling event hooks.
 * Uses a ConcurrentHashMap to store the subscribers for thread-safe access.
 * Each event class is mapped to a list of DefaultListeners.
 * @author pvpb0t
 */
public class EventManager {

	/**
	 * The subscribers map holds a list of listeners for each event type.
	 * The ConcurrentHashMap class is used to ensure thread-safety.
	 */
	private final ConcurrentHashMap<Class<? extends BaseEvent>, ArrayList<DefaultListener>> subscribers = new ConcurrentHashMap<>();

	// original code before i tried fixing an ide warning via ai: https://chat.openai.com/share/60845b26-3401-4a19-8246-520bd78659d2
	/**
	 * Subscribes an object to events that it has specified methods for.
	 * This method scans the object's class and its superclasses for methods that are annotated with @EventHook.
	 * For each such method, it creates a DefaultListener object with the method and the event class that it handles.
	 * The listener is added to the subscribers map for the corresponding event class.
	 * If the event class has any inner classes that extend it, the method is also added as a listener for those classes.
	 *
	 * @param object the object to be subscribed to events.
	 */
	public void subscribe(final Object object) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.isAnnotationPresent(SubscribeEvent.class)) {
					final SubscribeEvent subscribeEvent = method.getAnnotation(SubscribeEvent.class);
					final int prio = subscribeEvent.priority();
					Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes.length > 0 && BaseEvent.class.isAssignableFrom(parameterTypes[0])) {
						final Class<?> eventClazz = parameterTypes[0];
						if (BaseEvent.class.isAssignableFrom(eventClazz)) {
							final Class<? extends BaseEvent> safeEventClazz = eventClazz.asSubclass(BaseEvent.class);
							DefaultListener listener = new DefaultListener(safeEventClazz, method);
							listener.setPrio(prio);
							listener.setSource(object);
							subscribers.computeIfAbsent(safeEventClazz, k -> new ArrayList<>()).add(listener);

							for (Class<?> subClazz : eventClazz.getDeclaredClasses()) {
								if (eventClazz.isAssignableFrom(subClazz) && BaseEvent.class.isAssignableFrom(subClazz)) {
									final Class<? extends BaseEvent> safeSubClazz = subClazz.asSubclass(BaseEvent.class);
									DefaultListener subListener = new DefaultListener(safeSubClazz, method);
									subListener.setPrio(prio);
									subListener.setSource(object);
									subscribers.computeIfAbsent(safeSubClazz, k -> new ArrayList<>()).add(subListener);
								}
							}
						}
					}
				}
			}
			clazz = clazz.getSuperclass();
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
	public void publish(BaseEvent event) {
		Class<?> clazz = event.getClass();
		while (clazz != null) {
			if (subscribers.containsKey(clazz)) {
				final ArrayList<DefaultListener> listenersCopy = new ArrayList<>(subscribers.get(clazz));
				for (DefaultListener listener : listenersCopy) {
					try {
						MethodHandles.Lookup lookup = MethodHandles.lookup();
						MethodHandle handle = lookup.unreflect(listener.getTarget());
						handle.invoke(listener.getSource(), event);
					} catch (Throwable e) {
						DevUtils.sendErrorMessage(e, "An error occurred publishing a " + clazz.getSimpleName());
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}


	/**
	 * Unsubscribes the given object from all events it was previously subscribed to.
	 * This method removes all listeners that have the given object as their source from the subscribers map.
	 * It also removes any event classes that no longer have any listeners.
	 * If an event class has any inner classes that extend it, it also removes any listeners for those classes.
	 *
	 * @param object the object to unsubscribe
	 */
	public void unsubscribe(final Object object) {
		subscribers.values().forEach(listeners -> listeners.removeIf(listener -> listener.getSource() == object));
		subscribers.entrySet().removeIf(entry -> entry.getValue().isEmpty());
		subscribers.keySet().stream()
			.filter(clazz -> clazz.getDeclaredClasses().length > 0)
			.flatMap(clazz -> Arrays.stream(clazz.getDeclaredClasses()))
			.filter(subClazz -> subClazz.isAssignableFrom(subClazz.getDeclaringClass()))
			.forEach(subClazz -> {
				ArrayList<DefaultListener> subListeners = subscribers.get(subClazz);
				if (subListeners != null) {
					subListeners.removeIf(listener -> listener.getSource() == object);
					if (subListeners.isEmpty()) {
						subscribers.remove(subClazz);
					}
				}
			});
	}
}