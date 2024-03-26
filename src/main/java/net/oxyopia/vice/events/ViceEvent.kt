package net.oxyopia.vice.events

/**
 * An abstract class for events that can be listened to and cancelled.
 * Subclasses should override the appropriate methods to define the event's behavior.
 *
 * @author pvpb0t
 * @author Oxyopiia
 */
abstract class ViceEvent {
	abstract class Cancelable<T: Any> : ViceEvent() {
		private var returnValue: T? = null
		var isCanceled: Boolean = false

		open fun cancel() {
			isCanceled = true
		}

		fun hasReturnValue(): Boolean {
			return this.returnValue != null
		}

		fun setReturnValue(value: T) {
			isCanceled = true
			returnValue = value
		}

		fun getReturnValue(): T? {
			return this.returnValue
		}
	}

	open fun onceSent() {}
}
