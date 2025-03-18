package com.github.justincranford.anagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Uses a variation of <A HREF="https://en.wikipedia.org/wiki/Counting_sort">Counting Sort</A> to compute an ordered histogram of
     * the unique characters in a string. Caller can use equals or hashCode to compare words and see if they are anagrams of each other.
     * <P>
     * It works by creating an ordered map of unique characters and their counts; the characters are handled as Unicode code points (aka UTF32).
     * The code points and their counts are serialized as a CSV string.
     * The CSV is a deterministic-ordered histogram of the unique characters of the string and their counts.
     * <P>
     * The histogram approach is better than the naive approach of converting to char[] and sorting that array.
     * <UL>Naive sort of value.toCharArray() doesn't deduplicate the characters, so worst case timing is higher than sorting unique characters.
     * <UL>Naive sort of UTF-16 code units corrupts the order of surrogate pairs, using Unicode code points (aka UTF32) preserves the characters of the string.
     * <P>
     * @param value Non-null string of arbitrary length. Handles empty or whitespace strings.
     * @return Ordered CSV histogram representing the unique characters and their corresponding counts.
     */
    public static String serializedHistogram(final String value) {
        final Map<Integer, Integer> codePointCounts = new HashMap<>();
        value.codePoints().forEach(codePoint -> codePointCounts.merge(codePoint, 1, Integer::sum));
        final List<Map.Entry<Integer, Integer>> sortedEntries = codePointCounts.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
        final List<String> csvCodePointAndCount = sortedEntries.stream().map(e -> e.getKey() + "=" + e.getValue()).toList();
        return String.join(",", csvCodePointAndCount);
    }
}
