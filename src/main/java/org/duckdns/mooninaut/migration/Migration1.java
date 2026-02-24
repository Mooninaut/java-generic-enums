package org.duckdns.mooninaut.migration;

import org.duckdns.mooninaut.genericEnum.MyEnum;
import org.duckdns.mooninaut.genericEnum.MyEnumSet;

import java.util.List;
import java.util.Map;

/**
 * Step 1
 * <ul>
 * <li>Library changes method parameters of type {@code EnumSet<X>} to {@code EnumSet<? extends X>}.
 *     This change is source-compatible for clients.</li>
 * <li>Casts from {@code EnumSet<? extends X>} to {@code EnumSet<X>} may be required inside library methods.</li>
 * <li>Suggestion: Some sort of {@code @MigratingToGenericEnum} annotation to prompt clients to update references.</li>
 * </ul>
 */
public abstract sealed class Migration1
        extends MyEnum<Migration1>
        permits Migration1.A, Migration1.B
{
    protected Migration1(String name, int ordinal) {
        super(name, ordinal);
    }

    public static final A A = new A();
    public static final B B = new B();

    public static final List<Migration1> VALUES = List.of(A, B);
    public static final Map<String, Migration1> DIRECTORY = makeDirectory(VALUES);

    public MyEnumSet<Migration1> toEnumSet() {
        return MyEnumSet.of(this);
    }

    public static MyEnumSet<Migration1> getEnumSet() {
        return MyEnumSet.allOf(Migration1.class);
    }

    // MyEnumSet<Migration1> set -> MyEnumSet<? extends Migration1> set
    public static MyEnumSet<Migration1> invert(MyEnumSet<? extends Migration1> set) {
        MyEnumSet<Migration1> all = getEnumSet();
        all.removeAll(set);
        return all;
    }

    public abstract Number getNumber();

    // MyEnumSet<Migration1> set -> MyEnumSet<? extends Migration1> set
    public static void printEnumSet(MyEnumSet<? extends Migration1> set) {
        System.out.println(set);
    }

    public static Migration1[] values() {
        return VALUES.toArray(new Migration1[0]);
    }

    public static final class A extends Migration1 {
        private A() {
            super("A", 0);
        }

        public Integer getNumber() {
            return -1;
        }
    }

    public static final class B extends Migration1 {
        private B() {
            super("B", 1);
        }

        public Double getNumber() {
            return Math.PI;
        }
    }

    public static void main(String[] args) {
        MyEnumSet<Migration1> setAll = getEnumSet();
        printEnumSet(setAll);
        setAll = MyEnumSet.allOf(Migration1.class);
        printEnumSet(setAll);
        System.out.println(setAll.iterator().next().getNumber());

        MyEnumSet<Migration1> setB = B.toEnumSet();
        printEnumSet(setB);
        setB = invert(setB);
        printEnumSet(setB);
        System.out.println(setB.iterator().next().getNumber());
    }
}
