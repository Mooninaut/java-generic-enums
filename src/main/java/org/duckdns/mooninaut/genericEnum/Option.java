package org.duckdns.mooninaut.genericEnum;

import java.util.List;
import java.util.Locale;
import java.util.Map;

// The key to making this work is that Option<T> extends MyEnum<Option<?>>
// instead of MyEnum<Option<T>>. Changing ? to T breaks the class.
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

    public static final List<Option<?>> VALUES = List.of(D, PROC);
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

    public static Option<?> valueOf(String name) {
        return DIRECTORY.get(name);
    }
}
