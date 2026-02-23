package org.duckdns.mooninaut.genericEnum;

import java.util.*;

import static java.util.stream.Collectors.toCollection;

public abstract sealed class Argument<X>
        extends MyEnum<Argument<?>>
        permits Argument.STRING_ARGUMENT, Argument.INTEGER_ARGUMENT, Argument.LONG_ARGUMENT
{
    protected Argument(String name, int ordinal) {
        super(name, ordinal);
    }

    public static STRING_ARGUMENT STRING_ARGUMENT = new STRING_ARGUMENT();
    public static INTEGER_ARGUMENT INTEGER_ARGUMENT = new INTEGER_ARGUMENT();
    public static LONG_ARGUMENT LONG_ARGUMENT = new LONG_ARGUMENT();
    public abstract Class<X> getClazz();

    public static final List<Argument<?>> VALUES = List.of(STRING_ARGUMENT, INTEGER_ARGUMENT, LONG_ARGUMENT);
    public static final Map<String, Argument<?>> DIRECTORY = makeDirectory(VALUES);

    public static Argument<?> valueOf(String name) {
        return DIRECTORY.get(name);
    }

    public static Argument<?>[] values() {
        return VALUES.toArray(new Argument[0]);
    }

    public static final class STRING_ARGUMENT extends Argument<String> {
        STRING_ARGUMENT() {
            super("STRING_ARGUMENT", 0);
        }

        @Override
        public Class<String> getClazz() {
            return String.class;
        }
    }
    
    public static final class INTEGER_ARGUMENT extends Argument<Integer> {
        INTEGER_ARGUMENT() {
            super("INTEGER_ARGUMENT", 1);
        }
        
        @Override
        public Class<Integer> getClazz() {
            return Integer.class;
        }
    }

    public static final class LONG_ARGUMENT extends Argument<Long> {
        LONG_ARGUMENT() {
            super("LONG_ARGUMENT", 2);
        }

        @Override
        public Class<Long> getClazz() {
            return Long.class;
        }
    }

    public static void main(String[] args) {
        final var numberArguments = Arrays.stream(Argument.values())
                .filter(a -> Number.class.isAssignableFrom(a.getClazz()))
                .collect(toCollection(() -> MyEnumSet.noneOf(Argument.STRING_ARGUMENT.getDeclaringClass())));

        System.out.println(numberArguments);
    }
}
