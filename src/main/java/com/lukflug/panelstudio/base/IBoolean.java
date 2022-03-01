package com.lukflug.panelstudio.base;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Interface representing something returning a boolean.
 * @author lukflug
 */
@FunctionalInterface
public interface IBoolean extends BooleanSupplier,Supplier<Boolean>,Predicate<Void> {
	/**
	 * Get the value of the boolean.
	 * @return a boolean value
	 */
    boolean isOn();
	
	@Override
    default boolean getAsBoolean() {
		return isOn();
	}
	
	@Override
    default Boolean get() {
		return isOn();
	}
	
	@Override
    default boolean test(Void t) {
		return isOn();
	}
}
