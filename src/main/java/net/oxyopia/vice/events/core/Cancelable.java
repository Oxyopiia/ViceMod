package net.oxyopia.vice.events.core;

import net.oxyopia.vice.events.BaseEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that the specified event can be cancelled with {@link BaseEvent#setCanceled(boolean)}
 */
@Retention(value = RUNTIME)
@Target(value = TYPE)
public @interface Cancelable{}
