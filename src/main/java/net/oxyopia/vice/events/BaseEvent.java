package net.oxyopia.vice.events;

/**
 An abstract class for events that can be listened to and cancelled.
 Subclasses should override the appropriate methods to define the event's behavior.
 @author pvpb0t
 */
public abstract class BaseEvent {

	/**
	 Indicates whether the event has been cancelled.
	 @return true if the event is cancelled, false otherwise.
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 Sets the cancelled state of the event.
	 @param way the new cancelled state of the event.
	 */
	public void setCancelled(boolean way) {
		this.cancelled = way;
	}

	/**
	 Represents the cancelled state of the event.
	 */
	private boolean cancelled = false;

}
