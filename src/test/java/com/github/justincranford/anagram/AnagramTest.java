package com.github.justincranford.anagram;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.lang.Character.MAX_CODE_POINT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AnagramTest {
    private final int repeats = 10;
    private final Random random = new Random();

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

    @Nested
    @Order(1)
    public class PerfCompareLowUniqueness extends PerfCompare {
        public PerfCompareLowUniqueness() {
            super(lowUniquenessString(1_000_000));
        }
    }

    @Nested
    @Order(2)
    public class PerfCompareMediumUniqueness extends PerfCompare {
        public PerfCompareMediumUniqueness() {
            super(highUniquenessString(1_000_000, 1_000));
        }
    }

    @Nested
    @Order(3)
    public class PerfCompareHighUniqueness extends PerfCompare {
        public PerfCompareHighUniqueness() {
            super(highUniquenessString(1_000_000, 100_000));
        }
    }

    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public abstract class PerfCompare {
        private final String string;

        public PerfCompare(final String string) {
            this.string = string;
            System.out.println("Unique code points: " + AnagramTest.countUniqueCodePoints(string));

            // warm up
            Anagram.serializedHistogram(string);
            AnagramTest.serializedHistogram_countingSortArray(string);
            AnagramTest.codePoints_naiveSort(string);
            AnagramTest.charArray_naiveSort(string);
        }

        @Order(1)
        @Test // Fastest using codePoints, because speed optimized using int array of counts instead of TreeMap
        public void serializedHistogram_countingSort_IntArray() {
            for (int i = 0; i< repeats; i++) {
                AnagramTest.serializedHistogram_countingSortArray(string);
            }
        }

        @Order(2)
        @Test // Second fastest using codePoints, because memory optimized using TreeMap instead of int array of counts
        public void serializedHistogram_countingSort_TreeMap() {
            for (int i = 0; i< repeats; i++) {
                Anagram.serializedHistogram(string);
            }
        }

        @Order(3)
        @Test // Slowest using codePoints, easy to implement using int array of counts
        public void codePoints_naiveSort_IntArray() {
            for (int i = 0; i< repeats; i++) {
                AnagramTest.codePoints_naiveSort(string);
            }
        }

        @Order(4)
        @Test // Fastest overall, but corrupts order of UTF-16 surrogate pairs
        public void charArray_naiveSort_CharArray() {
            for (int i = 0; i< repeats; i++) {
                AnagramTest.charArray_naiveSort(string);
            }
        }
    }

    private String lowUniquenessString(final int length) {
        final byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }

    public String highUniquenessString(final int length, final int uniqueness) {
        final int[] codePoints = new int[length];
        for (int i = 0; i < length; i++) {
            codePoints[i] = random.nextInt(uniqueness);
        }
        return new String(codePoints, 0, codePoints.length);
    }

    private static int countUniqueCodePoints(final String value) {
        final Set<Integer> uniqueCodePoints = new HashSet<>();
        value.codePoints().forEach(uniqueCodePoints::add);
        return uniqueCodePoints.size();
    }

    private static String serializedHistogram_countingSortArray(final String value) {
        final int maxCodePoint = value.codePoints().max().orElse(MAX_CODE_POINT); // 0X10FFFF
        final int[] counts = new int[maxCodePoint + 1];
        value.codePoints().forEach(codePoint -> counts[codePoint]++);

        final List<String> csvCodePointAndCount = new ArrayList<>(counts.length);
        for (int codePoint = 0; codePoint < counts.length; codePoint++) {
            if (counts[codePoint] > 0) {
                csvCodePointAndCount.add(codePoint + "=" + counts[codePoint]);
            }
        }
        return String.join(",", csvCodePointAndCount);
    }

    private static String codePoints_naiveSort(final String value) {
        final int[] codePoints = value.codePoints().toArray();
        Arrays.parallelSort(codePoints);
        return new String(codePoints, 0, codePoints.length);
    }

    private static String charArray_naiveSort(final String value) {
        final char[] chars = value.toCharArray();
        Arrays.parallelSort(chars);
        return new String(chars);
    }
}
