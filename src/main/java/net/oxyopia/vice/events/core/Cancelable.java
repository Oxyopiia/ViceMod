package net.oxyopia.vice.events.core;

import net.oxyopia.vice.events.ViceEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Denotes that the specified event can be cancelled with {@link ViceEvent.Cancelable#cancel()}
 */
@Retention(value = RUNTIME)
@Target(value = TYPE)
public @interface Cancelable{}