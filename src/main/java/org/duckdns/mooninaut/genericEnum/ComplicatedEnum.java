// ComplicatedEnum.java by Clement Cherlin is marked CC0 1.0.
//
// To view a copy of this mark, visit https://creativecommons.org/publicdomain/zero/1.0/
//
// See LICENSE-CC0 in this repository for the full text of the license

package org.duckdns.mooninaut.genericEnum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract sealed class ComplicatedEnum<T extends Number, U extends List<T>>
        extends MyEnum<ComplicatedEnum<?, ?>>
        permits ComplicatedEnum.INTEGER, ComplicatedEnum.LONG
{
    public ComplicatedEnum(String name, int ordinal) {
        super(name, ordinal);
    }

    public abstract U makeList(T... array);

    public static final INTEGER INTEGER = new INTEGER();
    public static final LONG LONG = new LONG();

    public static final List<ComplicatedEnum<?, ?>> VALUES = List.of(INTEGER, LONG);
    public static final Map<String, ComplicatedEnum<?, ?>> DIRECTORY = makeDirectory(VALUES);

    public static ComplicatedEnum<?, ?> valueOf(final String name) {
        return DIRECTORY.get(name);
    }

    public static final class INTEGER extends ComplicatedEnum<Integer, ArrayList<Integer>> {
        private INTEGER() {
            super("INTEGER", 0);
        }
        @Override
        public ArrayList<Integer> makeList(Integer... integers) {
            return new ArrayList<>(List.of(integers));
        }
    }

    public static final class LONG extends ComplicatedEnum<Long, LinkedList<Long>> {
        private LONG() {
            super("LONG", 1);
        }
        @Override
        public LinkedList<Long> makeList(Long... longs) {
            return new LinkedList<>(List.of(longs));
        }
    }
}
