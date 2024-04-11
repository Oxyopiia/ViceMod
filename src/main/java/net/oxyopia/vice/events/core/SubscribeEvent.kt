package net.oxyopia.vice.events.core

/**
 * Hooks a method to any [ViceEvent][net.oxyopia.vice.events.ViceEvent] specified in the method's first parameter.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SubscribeEvent