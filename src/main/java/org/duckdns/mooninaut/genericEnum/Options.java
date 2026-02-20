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
