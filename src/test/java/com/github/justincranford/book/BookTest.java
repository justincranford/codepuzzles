package com.github.justincranford.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BookTest {
    @Test
    public void testCompareToEqual() {
        Book book1 = new Book(2000, "Author 1", "Title 1");
        Book book2 = new Book(2000, "Author 1", "Title 1");
        assertEquals(0, book1.compareTo(book2));
    }

    @Test
    public void testCompareToYearPublished() {
        Book book1 = new Book(2000, "Author 1", "Title 1");
        Book book2 = new Book(1999, "Author 2", "Title 2");
        assertTrue(book1.compareTo(book2) > 0);
    }

    @Test
    public void testCompareToTitle() {
        Book book1 = new Book(2000, "Author 1", "Title 1");
        Book book2 = new Book(2000, "Author 2", "Title 2");
        assertTrue(book1.compareTo(book2) < 0);
    }

    @Test
    public void testCompareToAuthor() {
        Book book1 = new Book(2000, "Author 1", "Title 1");
        Book book2 = new Book(2000, "Author 2", "Title 1");
        assertTrue(book1.compareTo(book2) < 0);
    }

    @Test
    public void testCompareToNullBook() {
        Book book1 = new Book(2000, "Author 1", "Title 1");
        Book book2 = null;
        assertTrue(book1.compareTo(book2) > 0);
    }

    @Test
    public void testCompareToNullTitle() {
        Book book1 = new Book(2000, "Author 1", null);
        Book book2 = new Book(2000, "Author 1", "Title 2");
        assertTrue(book1.compareTo(book2) < 0);
    }

    @Test
    public void testCompareToNullAuthor() {
        Book book1 = new Book(2000, null, "Title 1");
        Book book2 = new Book(2000, "Author 2", "Title 1");
        assertTrue(book1.compareTo(book2) < 0);
    }
}
