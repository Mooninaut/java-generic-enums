package org.duckdns.mooninaut.migration;

import org.duckdns.mooninaut.genericEnum.MyEnum;
import org.duckdns.mooninaut.genericEnum.MyEnumSet;

import java.util.List;
import java.util.Map;

/**
 * Step 5 (optional)
 * <ul>
 * <li>Library replaces {@code EnumSet<? extends X>} with {@code EnumSet<X<?>>} in method parameter types.</li>
 * <li>Library methods can no longer be called with values of type {@code EnumSet<? extends X>}.</li>
 * <li>This is source-compatible with clients that have completed step 4.</li>
 * <li>No further client changes are required.</li>
 * <li>Casts to {@code EnumSet<X<?>>} are no longer required.</li>
 * <li>This step is optional. The only benefit is removing casts. Libraries can delay this step
 * as long as desired to allow clients time to complete step 4.</li>
 * </ul>
 */
public abstract sealed class Migration5<T extends Number>
        extends MyEnum<Migration5<?>>
        permits Migration5.A, Migration5.B
{
    protected Migration5(String name, int ordinal) {
        super(name, ordinal);
    }

    public static final A A = new A();
    public static final B B = new B();

    public static final List<Migration5<?>> VALUES = List.of(A, B);
    public static final Map<String, Migration5<?>> DIRECTORY = makeDirectory(VALUES);

    public MyEnumSet<Migration5<?>> toEnumSet() {
        return MyEnumSet.of(this);
    }

    public static MyEnumSet<Migration5<?>> getEnumSet() {
        return MyEnumSet.allOf(Migration5.A.getDeclaringClass());
    }

    // MyEnumSet<? extends Migration5> -> MyEnumSet<Migration5<?>>
    public static MyEnumSet<Migration5<?>> invert(MyEnumSet<Migration5<?>> set) {
        MyEnumSet<Migration5<?>> all = getEnumSet();
        all.removeAll(set);
        return all;
    }

    public abstract T getNumber();

    // MyEnumSet<? extends Migration5> -> MyEnumSet<Migration5<?>>
    public static void printEnumSet(MyEnumSet<Migration5<?>> set) {
        System.out.println(set);
    }

    public static Migration5<?>[] values() {
        return VALUES.toArray(new Migration5[0]);
    }

    public static final class A extends Migration5<Integer> {
        private A() {
            super("A", 0);
        }

        public Integer getNumber() {
            return -1;
        }
    }

    public static final class B extends Migration5<Double> {
        private B() {
            super("B", 1);
        }

        public Double getNumber() {
            return Math.PI;
        }
    }

    public static void main(String[] args) {
        // MyEnumSet<? extends Migration5> -> MyEnumSet<Migration5<?>>
        MyEnumSet<Migration5<?>> setAll = getEnumSet();
        printEnumSet(setAll);
        // MyEnumSet.allOf(Migration5.class) -> MyEnumSet.allOf(A.getDeclaringClass())
        setAll = MyEnumSet.allOf(A.getDeclaringClass());
        printEnumSet(setAll);
        System.out.println(setAll.iterator().next().getNumber());

        // MyEnumSet<? extends Migration5> -> MyEnumSet<Migration5<?>>
        MyEnumSet<Migration5<?>> setB = B.toEnumSet();
        printEnumSet(setB);
        setB = invert(setB);
        printEnumSet(setB);
        System.out.println(setB.iterator().next().getNumber());
    }
}
