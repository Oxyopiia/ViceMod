package net.oxyopia.vice.events.core;

import net.oxyopia.vice.events.BaseEvent;

import java.lang.reflect.Method;

/**
 A listener class that contains information about the event it listens to, its target method, and its source object.
 The listener can be constructed with a specified event, target method.
 The source object can be set through the setSource method.
 The target method can be retrieved with the getTarget method.
 The event class can be retrieved with the getEvent method.
 The source object can be retrieved with the getSource method.
 @author pvpb0t
 */
public class DefaultListener{
	private Class<? extends BaseEvent> event;
	private Method target;
	private Object source;

	/**
	 Returns the source object of this listener.
	 @return the source object of this listener
	 */
	public Object getSource() {
		return source;
	}

	/**
	 Sets the source object of this listener.
	 @param source the new source object of this listener
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	/**
	 Constructs a new DefaultListener with the specified event and target method.
	 @param event the event class that this listener listens to
	 @param target the target method that this listener calls when the event is fired
	 */
	public DefaultListener(Class<? extends BaseEvent> event, Method target) {
		this.event = event;
		this.target = target;
	}

	/**
	 Sets the event class that this listener listens to.
	 @param event the new event class of this listener
	 */
	public void setEvent(Class<? extends BaseEvent> event) {
		this.event = event;
	}

	/**
	 Returns the target method that this listener calls when the event is fired.
	 @return the target method of this listener
	 */
	public Method getTarget() {
		return target;
	}

	/**
	 Sets the target method that this listener calls when the event is fired.
	 @param target the new target method of this listener
	 */
	public void setTarget(Method target) {
		this.target = target;
	}

	/**
	 Returns the event class that this listener listens to.
	 @return the event class of this listener
	 */
	public Class<? extends BaseEvent> getEvent() {
		return event;
	}
}
