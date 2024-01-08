package net.oxyopia.vice.events;

/**
 An abstract class for events that can be listened to and cancelled.
 Subclasses should override the appropriate methods to define the event's behavior.

 @author pvpb0t
 @author Oxyopiia
 */
public abstract class BaseEvent {

	private boolean cancelable = false;
	private boolean returnable = false;
	private boolean canceled = false;
	private Object returnValue;

	/**
	 Indicates whether the event has been cancelled.
	 @return true if the event is cancelled, false otherwise.
	 */
	public boolean isCanceled() {
		return this.canceled;
	}

	/**
	 Sets the cancelled state of the event.
	 @param way the new cancelled state of the event.
	 */
	public void setCanceled(boolean way) {
		if (cancelable) this.canceled = way;
	}

	public void setCancelable(boolean val) {
		this.cancelable = val;
	}

	public boolean isCancelable() {
		return this.cancelable;
	}

	public void setReturnable(boolean val) {
		this.returnable = val;
	}

	public boolean isReturnable() {
		return this.returnable;
	}

	public void setReturnValue(Object value) {
		if (this.returnable && this.cancelable) {
			this.setCanceled(true);
			this.returnValue = value;
		}
	}

	public Object getReturnValue() {
		if (this.returnable) return this.returnValue;
		return null;
	}
}
