package org.duckdns.mooninaut.migration;

import org.duckdns.mooninaut.genericEnum.MyEnum;
import org.duckdns.mooninaut.genericEnum.MyEnumSet;

import java.util.List;
import java.util.Map;

/**
 * Step 3
 * <ul>
 * <li>Library makes X generic, updates methods that return {@code EnumSet<X>} to return {@code EnumSet<X<?>>}
 *     This is source-compatible for clients that have completed step 2, with an unchecked warning on
 *     {@code EnumSet.allOf(X.class)} and related methods.</li>
 * <li>Library methods may require casts from {@code EnumSet<? extends X>} to {@code EnumSet<X<?>>}</li>
 * <li>Library changes {@code EnumSet.allOf(X.class)} to {@code EnumSet.allOf(X.CONSTANT.getDeclaringClass())}</li>
 * </ul>
 */
// Migration3 -> Migration3<T extends Number>
public abstract sealed class Migration3<T extends Number>
        extends MyEnum<Migration3<?>>
        permits Migration3.A, Migration3.B
{
    protected Migration3(String name, int ordinal) {
        super(name, ordinal);
    }

    public static final A A = new A();
    public static final B B = new B();

    public static final List<Migration3<?>> VALUES = List.of(A, B);
    public static final Map<String, Migration3<?>> DIRECTORY = makeDirectory(VALUES);

    // MyEnumSet<? extends Migration3> -> MyEnumSet<Migration3<?>>
    public MyEnumSet<Migration3<?>> toEnumSet() {
        return MyEnumSet.of(this);
    }

    // MyEnumSet<? extends Migration3> -> MyEnumSet<Migration3<?>>
    public static MyEnumSet<Migration3<?>> getEnumSet() {
        return MyEnumSet.allOf(Migration3.A.getDeclaringClass());
    }

    // MyEnumSet<? extends Migration3> -> MyEnumSet<Migration3<?>>
    public static MyEnumSet<Migration3<?>> invert(MyEnumSet<? extends Migration3> set) {
        // MyEnumSet<? extends Migration3> -> MyEnumSet<Migration3<?>>
        MyEnumSet<Migration3<?>> all = getEnumSet();
        all.removeAll(set);
        return all;
    }

    // Number -> T
    public abstract T getNumber();

    public static void printEnumSet(MyEnumSet<? extends Migration3> set) {
        System.out.println(set);
    }

    public static Migration3<?>[] values() {
        return VALUES.toArray(new Migration3[0]);
    }

    // Migration3 -> Migration3<Integer>
    public static final class A extends Migration3<Integer> {
        private A() {
            super("A", 0);
        }

        public Integer getNumber() {
            return -1;
        }
    }

    // Migration3 -> Migration3<Double>
    public static final class B extends Migration3<Double> {
        private B() {
            super("B", 1);
        }

        public Double getNumber() {
            return Math.PI;
        }
    }

    public static void main(String[] args) {
        MyEnumSet<? extends Migration3> setAll = getEnumSet();
        printEnumSet(setAll);
        setAll = MyEnumSet.allOf(Migration3.class);
        printEnumSet(setAll);
        System.out.println(setAll.iterator().next().getNumber());

        MyEnumSet<? extends Migration3> setB = B.toEnumSet();
        printEnumSet(setB);
        setB = invert(setB);
        printEnumSet(setB);
        System.out.println(setB.iterator().next().getNumber());
    }
}
