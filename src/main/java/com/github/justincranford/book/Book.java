package com.github.justincranford.book;

public class Book implements Comparable<Book> {
    private final int yearPublished;
    private final String author;
    private final String title;

    public Book(final int yearPublished, final String author, final String title) {
        this.yearPublished = yearPublished;
        this.author = author;
        this.title = title;
    }

    @Override
    public int compareTo(Book otherBook) {
        if (otherBook == null) {
            return 1;
        }
        final int yearComparison = Integer.compare(this.yearPublished, otherBook.yearPublished);
        if (yearComparison != 0) {
            return yearComparison;
        }
        final int titleComparison = (this.title == null && otherBook.title == null) ? 0 :
                                    (this.title == null) ? -1 :
                                    (otherBook.title == null) ? 1 :
                                    this.title.compareTo(otherBook.title);
        if (titleComparison != 0) {
            return titleComparison;
        }
        return (this.author == null && otherBook.author == null) ? 0 :
               (this.author == null) ? -1 :
               (otherBook.author == null) ? 1 :
               this.author.compareTo(otherBook.author);
    }
}
