package org.duckdns.mooninaut.migration;

import org.duckdns.mooninaut.genericEnum.MyEnum;
import org.duckdns.mooninaut.genericEnum.MyEnumSet;

import java.util.List;
import java.util.Map;

/**
 * Step 2
 * <ul>
 * <li>Clients update library, and change variables of type {@code EnumSet<X>} to {@code EnumSet<? extends X>}</li>
 * <li>Both {@code EnumSet<X>} and {@code EnumSet<? extends X>} compile.</li>
 * </ul>
 */
public abstract sealed class Migration2
        extends MyEnum<Migration2>
        permits Migration2.A, Migration2.B
{
    protected Migration2(String name, int ordinal) {
        super(name, ordinal);
    }

    public static final A A = new A();
    public static final B B = new B();

    public static final List<Migration2> VALUES = List.of(A, B);
    public static final Map<String, Migration2> DIRECTORY = makeDirectory(VALUES);

    public MyEnumSet<Migration2> toEnumSet() {
        return MyEnumSet.of(this);
    }

    public static MyEnumSet<Migration2> getEnumSet() {
        return MyEnumSet.allOf(Migration2.class);
    }

    public static MyEnumSet<Migration2> invert(MyEnumSet<? extends Migration2> set) {
        MyEnumSet<Migration2> all = getEnumSet();
        all.removeAll(set);
        return all;
    }

    public abstract Number getNumber();

    public static void printEnumSet(MyEnumSet<? extends Migration2> set) {
        System.out.println(set);
    }

    public static Migration2[] values() {
        return VALUES.toArray(new Migration2[0]);
    }

    public static final class A extends Migration2 {
        private A() {
            super("A", 0);
        }

        public Integer getNumber() {
            return -1;
        }
    }

    public static final class B extends Migration2 {
        private B() {
            super("B", 1);
        }

        public Double getNumber() {
            return Math.PI;
        }
    }

    public static void main(String[] args) {
        // MyEnumSet<Migration2> -> MyEnumSet<? extends Migration2>
        MyEnumSet<? extends Migration2> setAll = getEnumSet();
        printEnumSet(setAll);
        setAll = MyEnumSet.allOf(Migration2.class);
        printEnumSet(setAll);
        System.out.println(setAll.iterator().next().getNumber());

        // MyEnumSet<Migration2> -> MyEnumSet<? extends Migration2>
        MyEnumSet<? extends Migration2> setB = B.toEnumSet();
        printEnumSet(setB);
        setB = invert(setB);
        printEnumSet(setB);
        System.out.println(setB.iterator().next().getNumber());
    }
}
