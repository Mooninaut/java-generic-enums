package org.duckdns.mooninaut.migration;

import org.duckdns.mooninaut.genericEnum.MyEnum;
import org.duckdns.mooninaut.genericEnum.MyEnumSet;

import java.util.List;
import java.util.Map;

/**
 * Step 4
 * <ul>
 * <li>Clients update library, change usages of {@code EnumSet<? extends X>} to {@code EnumSet<X<?>>}
 *     and change {@code EnumSet.allOf(X.class)} to {@code EnumSet.allOf(X.CONSTANT.getDeclaringClass())}</li>
 * <li>Usages of {@code EnumSet<X>} now produce compilation errors.</li>
 * <li>Both {@code EnumSet<? extends X> set = EnumSet.allOf(X.class)} and
 *     {@code EnumSet<X<?>> set = EnumSet.allOf(X.CONSTANT.getDeclaringClass())} compile.</li>
 * </ul>
 */
public abstract sealed class Migration4<T extends Number>
        extends MyEnum<Migration4<?>>
        permits Migration4.A, Migration4.B
{
    protected Migration4(String name, int ordinal) {
        super(name, ordinal);
    }

    public static final A A = new A();
    public static final B B = new B();

    public static final List<Migration4<?>> VALUES = List.of(A, B);
    public static final Map<String, Migration4<?>> DIRECTORY = makeDirectory(VALUES);

    public MyEnumSet<Migration4<?>> toEnumSet() {
        return MyEnumSet.of(this);
    }

    public static MyEnumSet<Migration4<?>> getEnumSet() {
        return MyEnumSet.allOf(Migration4.A.getDeclaringClass());
    }

    public static MyEnumSet<Migration4<?>> invert(MyEnumSet<? extends Migration4> set) {
        MyEnumSet<Migration4<?>> all = getEnumSet();
        all.removeAll(set);
        return all;
    }

    public abstract T getNumber();

    public static void printEnumSet(MyEnumSet<? extends Migration4> set) {
        System.out.println(set);
    }

    public static Migration4[] values() {
        return VALUES.toArray(new Migration4[0]);
    }

    public static final class A extends Migration4<Integer> {
        private A() {
            super("A", 0);
        }

        public Integer getNumber() {
            return -1;
        }
    }

    public static final class B extends Migration4<Double> {
        private B() {
            super("B", 1);
        }

        public Double getNumber() {
            return Math.PI;
        }
    }

    public static void main(String[] args) {
        // MyEnumSet<? extends Migration4> -> MyEnumSet<Migration4<?>>
        MyEnumSet<Migration4<?>> setAll = getEnumSet();
        printEnumSet(setAll);
        // MyEnumSet.allOf(Migration4.class) -> MyEnumSet.allOf(A.getDeclaringClass())
        setAll = MyEnumSet.allOf(A.getDeclaringClass());
        printEnumSet(setAll);
        System.out.println(setAll.iterator().next().getNumber());

        // MyEnumSet<? extends Migration4> -> MyEnumSet<Migration4<?>>
        MyEnumSet<Migration4<?>> setB = B.toEnumSet();
        printEnumSet(setB);
        setB = invert(setB);
        printEnumSet(setB);
        System.out.println(setB.iterator().next().getNumber());
    }
}
