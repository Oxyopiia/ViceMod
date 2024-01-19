package net.oxyopia.vice.events

/**
 * An abstract class for events that can be listened to and cancelled.
 * Subclasses should override the appropriate methods to define the event's behavior.
 *
 * @author pvpb0t
 * @author Oxyopiia
 */
abstract class ViceEvent {
	abstract class Cancelable<T: Any?> : ViceEvent() {
		private var returnValue: T? = null
		var canceled: Boolean = false

		fun cancel() {
			canceled = true
		}

		fun hasReturnValue(): Boolean {
			return this.returnValue != null
		}

		fun setReturnValue(value: T) {
			canceled = true
			returnValue = value
		}

		fun getReturnValue(): T? {
			return this.returnValue
		}
	}
}
