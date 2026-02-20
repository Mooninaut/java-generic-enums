// Main.java by Clement Cherlin is marked CC0 1.0.
//
// To view a copy of this mark, visit https://creativecommons.org/publicdomain/zero/1.0/
//
// See LICENSE-CC0 in this repository for the full text of the license

package org.duckdns.mooninaut;

import org.duckdns.mooninaut.genericEnum.MyEnum;
import org.duckdns.mooninaut.genericEnum.MyEnumSet;
import org.duckdns.mooninaut.genericEnum.Option;
import org.duckdns.mooninaut.genericEnum.Options;

import java.util.Map;

public class Main {

    public static final boolean USE_RAW_CAST = false;

    public static void main(String[] args) {
        final var map = Map.of(
                "-d", "Hello, World!",
                "-proc", "OnlY",
                "-g:", "vars,Lines"
        );

        System.out.println("map: " + map);
        System.out.println();

        final Options options = new Options(map);

        for (Option<?> option : Option.VALUES) {
            System.out.println(option + ": " + option.prefix() + " = " + options.get(option));
        }
        System.out.println();

        final MyEnumSet<Option<?>> set;
        if (USE_RAW_CAST) {
            // The only awkward part of using a generic enum
            set = MyEnumSet.allOf((Class<Option<?>>) (Class) Option.class);
        } else {
            // ergonomic workaround
            set = MyEnumSet.allOf(Option.D.getDeclaringClass());
        }

        System.out.println("MyEnumSet.allOf(Option): " + set);
        System.out.println();
        System.out.println("Option.valueOf(\"D\"): " + Option.valueOf("D"));
        System.out.println("MyEnum.valueOf(Option.class, \"PROC\"): " +
                MyEnum.valueOf(Option.class, "PROC"));
        System.out.println();
        System.out.println("D.getClass: " + Option.D.getClass());
        System.out.println("D.getDeclaringClass: " + Option.D.getDeclaringClass());
        System.out.println();
        System.out.println("Reversed values: " + Option.VALUES.reversed());
        System.out.println("Reversed values, sorted: " + Option.VALUES.reversed().stream().sorted().toList());
    }
}
