package com.github.justincranford.anagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class Anagram {
    private Anagram() { }

    public static List<List<String>> group(final String[] values) {
        if (values == null || values.length == 0) {
            return new ArrayList<>();
        }
        final Map<String,List<String>> grouped = new HashMap<>();
        for (final String value : values) {
            if (value == null) {
                throw new NullPointerException("Value can't be null");
            }
            final String serializedHistogram = serializedHistogram(value);
            if (!grouped.containsKey(serializedHistogram)) {
                grouped.put(serializedHistogram, new ArrayList<>());
            }
            grouped.get(serializedHistogram).add(value);
        }
        return new ArrayList<>(grouped.values());
    }


    public static String serializedHistogram(final String value) {
        final Map<Integer, Integer> codePointCounts = new TreeMap<>(); // deterministic order
        value.codePoints().forEach(codePoint -> {
            codePointCounts.put(codePoint, codePointCounts.getOrDefault(codePoint, 0) + 1);
        });
        final List<String> csvCodePointAndCount = codePointCounts.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).toList();
        return String.join(",", csvCodePointAndCount);
    }
}
