package com.github.justincranford.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BookTest {
    @Test
    public void testCompareToEqual() {
        final Book book1 = new Book(2000, "Author 1", "Title 1");
        final Book book2 = new Book(2000, "Author 1", "Title 1");
        assertEquals(0, book1.compareTo(book2));
    }

    @Test
    public void testCompareToYearPublished() {
        final Book book1 = new Book(2000, "Author 1", "Title 1");
        final Book book2 = new Book(1999, "Author 2", "Title 2");
        assertTrue(book1.compareTo(book2) > 0);
    }

    @Test
    public void testCompareToTitle() {
        final Book book1 = new Book(2000, "Author 1", "Title 1");
        final Book book2 = new Book(2000, "Author 2", "Title 2");
        assertTrue(book1.compareTo(book2) < 0);
    }

    @Test
    public void testCompareToAuthor() {
        final Book book1 = new Book(2000, "Author 1", "Title 1");
        final Book book2 = new Book(2000, "Author 2", "Title 1");
        assertTrue(book1.compareTo(book2) < 0);
    }

    @Test
    public void testCompareToNullBook() {
        final Book book1 = new Book(2000, "Author 1", "Title 1");
        assertTrue(book1.compareTo(null) > 0);
    }

    @Test
    public void testCompareToNullTitle() {
        final Book book1 = new Book(2000, "Author 1", null);
        final Book book2 = new Book(2000, "Author 1", "Title 2");
        assertTrue(book1.compareTo(book2) < 0);
    }

    @Test
    public void testCompareToNullAuthor() {
        final Book book1 = new Book(2000, null, "Title 1");
        final Book book2 = new Book(2000, "Author 2", "Title 1");
        assertTrue(book1.compareTo(book2) < 0);
    }
}
