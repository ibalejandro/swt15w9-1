package app.util;

import java.util.function.BinaryOperator;

/**
 * These are two helper functions which I needed for merge functions during map creation (in the value map etc)
 *
 * They do nothing other than taking two arbitrary inputs and selecting either the first or the second one.
 *
 * Created by justusadam on 05/12/15.
 */
@FunctionalInterface
public interface SelectorFunctions<U> extends BinaryOperator<U> {
    static <U> SelectorFunctions<U> first() {
        return (a, b) -> a;
    }

    static <U> SelectorFunctions<U> second() {
        return (a, b) -> b;
    }
}
