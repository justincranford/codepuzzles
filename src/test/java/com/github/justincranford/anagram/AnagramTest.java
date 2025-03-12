package com.github.justincranford.anagram;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnagramTest {
    @Test
    public void testDifferentWordsDifferentAnagrams() {
        final String[] input = { "eat", "tea", "tan", "ate", "nat", "bat" };
        final Set<List<String>> expected = Set.of(
            List.of("eat", "tea", "ate"),
            List.of("tan", "nat"),
            List.of("bat")
        );
        final List<List<String>> result = Anagram.group(input);
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    public void testDifferentUnicodeWordsDifferentAnagrams() {
        final String[] input = { "🍏🍎🍐", "🍐🍏🍎", "🍎🍐🍏", "🍱🍲🍳", "🍳🍱🍲", "🍔🍟🍕" };
        final Set<List<String>> expected = Set.of(
            List.of("🍏🍎🍐", "🍐🍏🍎", "🍎🍐🍏"),
            List.of("🍱🍲🍳", "🍳🍱🍲"),
            List.of("🍔🍟🍕")
        );
        final List<List<String>> result = Anagram.group(input);
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    public void testManyWordsOneAnagram() {
        final String[] input = { "a", "a", "a", "a", "a", "a" };
        final Set<List<String>> expected = Set.of(
            List.of("a", "a", "a", "a", "a", "a")
        );
        final List<List<String>> result = Anagram.group(input);
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    public void testOneWord() {
        final String[] input = { "a" };
        final Set<List<String>> expected = Set.of(
            List.of("a")
        );
        final List<List<String>> result = Anagram.group(input);
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    public void testOneEmptyWord() {
        final String[] input = { "" };
        final Set<List<String>> expected = Set.of(
            List.of("")
        );
        final List<List<String>> result = Anagram.group(input);
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    public void testNoWords() {
        final String[] input = { };
        final Set<List<String>> expected = Set.of();
        final List<List<String>> result = Anagram.group(input);
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    public void testNullValues() {
        final String[] input = null;
        final Set<List<String>> expected = Set.of();
        final List<List<String>> result = Anagram.group(input);
        assertTrue(result.containsAll(expected) && expected.containsAll(result));
    }

    @Test
    public void testNullValue() {
        final String[] input = { null };
        final NullPointerException npe = assertThrows(NullPointerException.class, () -> Anagram.group(input));
        assertEquals("Value can't be null", npe.getMessage());
    }
}
