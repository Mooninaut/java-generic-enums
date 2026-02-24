package org.duckdns.mooninaut.migration;

import org.duckdns.mooninaut.genericEnum.MyEnum;
import org.duckdns.mooninaut.genericEnum.MyEnumSet;

import java.util.List;
import java.util.Map;

public abstract sealed class Migration0
        extends MyEnum<Migration0>
        permits Migration0.A, Migration0.B
{
    protected Migration0(String name, int ordinal) {
        super(name, ordinal);
    }

    public static final A A = new A();
    public static final B B = new B();

    public static final List<Migration0> VALUES = List.of(A, B);
    public static final Map<String, Migration0> DIRECTORY = makeDirectory(VALUES);

    public MyEnumSet<Migration0> toEnumSet() {
        return MyEnumSet.of(this);
    }

    public static MyEnumSet<Migration0> getEnumSet() {
        return MyEnumSet.allOf(Migration0.class);
    }

    public static MyEnumSet<Migration0> invert(MyEnumSet<Migration0> set) {
        MyEnumSet<Migration0> all = getEnumSet();
        all.removeAll(set);
        return all;
    }

    public abstract Number getNumber();

    public static void printEnumSet(MyEnumSet<Migration0> set) {
        System.out.println(set);
    }

    public static Migration0[] values() {
        return VALUES.toArray(new Migration0[0]);
    }

    public static final class A extends Migration0 {
        private A() {
            super("A", 0);
        }

        public Integer getNumber() {
            return -1;
        }
    }

    public static final class B extends Migration0 {
        private B() {
            super("B", 1);
        }

        public Double getNumber() {
            return Math.PI;
        }
    }

    public static void main(String[] args) {
        MyEnumSet<Migration0> setAll = getEnumSet();
        printEnumSet(setAll);
        setAll = MyEnumSet.allOf(Migration0.class);
        printEnumSet(setAll);
        System.out.println(setAll.iterator().next().getNumber());

        MyEnumSet<Migration0> setB = B.toEnumSet();
        printEnumSet(setB);
        setB = invert(setB);
        printEnumSet(setB);
        System.out.println(setB.iterator().next().getNumber());
    }
}
