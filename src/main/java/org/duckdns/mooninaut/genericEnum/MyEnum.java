package org.duckdns.mooninaut.genericEnum;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class MyEnum<E extends MyEnum<E>> implements Comparable<E> {
    private final String name;

    public final String name() {
        return name;
    }

    private final int ordinal;

    public final int ordinal() {
        return ordinal;
    }

    protected MyEnum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public String toString() {
        return name;
    }

    public final boolean equals(Object other) {
        return this == other;
    }

    private int hash;

    public final int hashCode() {
        int hc = hash;
        if (hc == 0) {
            hc = hash = System.identityHashCode(this);
        }
        return hc;
    }

    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public final int compareTo(E o) {
        MyEnum<?> other = o;
        MyEnum<E> self = this;
        if (self.getClass() != other.getClass() && // optimization
                self.getDeclaringClass() != other.getDeclaringClass())
            throw new ClassCastException();
        return self.ordinal - other.ordinal;
    }

    @SuppressWarnings("unchecked")
    public final Class<E> getDeclaringClass() {
        Class<?> clazz = getClass();
        Class<?> zuper = clazz.getSuperclass();
        return (zuper == MyEnum.class) ? (Class<E>)clazz : (Class<E>)zuper;
    }

    @SuppressWarnings("unchecked")
    public static <T extends MyEnum<T>> T valueOf(Class<T> enumClass,
                                                  String name) {
        final Field directoryField;
        final Map<String, T> directory;
        try {
            directoryField = enumClass.getField("DIRECTORY");
            directory = (Map<String, T>) directoryField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(enumClass + " does not have valid DIRECTORY field", e);
        }
        T result = directory.get(name);
        if (result != null)
            return result;
        if (name == null)
            throw new NullPointerException("Name is null");
        throw new IllegalArgumentException(
                "No enum constant " + enumClass.getCanonicalName() + "." + name);
    }

    @SuppressWarnings("unchecked")
    static <T extends MyEnum<T>> T[] getUniverse(final Class<T> enumClass) {
        final Field valuesField;
        final List<T> values;
        try {
            valuesField = enumClass.getField("VALUES");
            values = (List<T>) valuesField.get(null);
            return values.toArray((T[]) Array.newInstance(enumClass, 0));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(enumClass + " does not have valid VALUES field", e);
        }
    }
    public static <T extends MyEnum<T>> Map<String, T> makeDirectory(List<T> values) {
        return values.stream().collect(Collectors.toUnmodifiableMap(MyEnum::name, Function.identity()));
    }
}
