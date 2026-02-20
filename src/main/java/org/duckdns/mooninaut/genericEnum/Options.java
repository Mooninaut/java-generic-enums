// Options.java by Clement Cherlin is marked CC0 1.0.
//
// To view a copy of this mark, visit https://creativecommons.org/publicdomain/zero/1.0/
//
// See LICENSE-CC0 in this repository for the full text of the license

package org.duckdns.mooninaut.genericEnum;

import java.util.Map;

public class Options {
    private final Map<String, String> map;

    public Options(Map<String, String> map) {
        this.map = map;
    }

    public <T> T get(Option<T> option) {
        return option.parseOption(map.get(option.prefix()));
    }
}
