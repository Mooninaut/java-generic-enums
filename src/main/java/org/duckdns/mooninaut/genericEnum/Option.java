// Option.java is based on https://mail.openjdk.org/pipermail/amber-spec-experts/2017-May/000041.html
//
// All original work in this file by Clement Cherlin is marked CC0 1.0.
//
// To view a copy of this mark, visit https://creativecommons.org/publicdomain/zero/1.0/
//
// See LICENSE-CC0 in this repository for the full text of the license

package org.duckdns.mooninaut.genericEnum;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// The key to making this work is that Option<T> extends MyEnum<Option<?>>
// instead of MyEnum<Option<T>>. Changing ? to T will break MyEnumSets of Option.
public sealed abstract class Option<T> extends MyEnum<Option<?>> {

    protected Option(String name, int ordinal, String prefix) {
        super(name, ordinal);
        this.prefix = prefix;
    }

    private final String prefix;

    public final String prefix() {
        return prefix;
    }

    public abstract T parseOption(String optionValue);

    public static final D D = new D();
    public static final PROC PROC = new PROC();
    public static final G_CUSTOM G_CUSTOM = new G_CUSTOM();

    public static final List<Option<?>> VALUES = List.of(D, PROC, G_CUSTOM);
    public static final Map<String, Option<?>> DIRECTORY = makeDirectory(VALUES);

    public static final class D extends Option<String> {
        private D() {
            super("D", 0, "-d");
        }
        public String parseOption(String optionValue) {
            return optionValue.trim();
        }
    }

    public enum ProcOption {
        NONE, ONLY
    }

    public static final class PROC extends Option<ProcOption> {
        private PROC() {
            super("PROC", 1, "-proc");
        }
        public ProcOption parseOption(String optionValue) {
            return ProcOption.valueOf(optionValue.toUpperCase(Locale.ROOT));
        }
    }

    public enum DebugOption {
        LINES, VARS, SOURCE
    }

    public static final class G_CUSTOM extends Option<EnumSet<DebugOption>> {
        private G_CUSTOM() {
            super("G_CUSTOM", 2, "-g:");
        }

        @Override
        public EnumSet<DebugOption> parseOption(String optionValue) {
            EnumSet<DebugOption> debugOptions = EnumSet.noneOf(DebugOption.class);
            for (String option : optionValue.split(",")) {
                debugOptions.add(DebugOption.valueOf(option.toUpperCase(Locale.ROOT)));
            }
            return debugOptions;
        }
    }

    public static Option<?> valueOf(String name) {
        return DIRECTORY.get(name);
    }
}
