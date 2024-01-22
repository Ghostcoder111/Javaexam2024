import lt.techin.library.Author;
import lt.techin.library.Book;
import lt.techin.library.BookCatalog;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;
import java.util.Comparator;
import java.math.BigDecimal;
//import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class MyBookCatalog implements BookCatalog {
    private final List<Book> bookList = new ArrayList<>();
    @Override
    public void addBook(Book book) {
        if(book == null) {
            throw new IllegalArgumentException("Book should not be null");
        }


        for(Book b : this.bookList) {
            if(b.getIsbn().equals(book.getIsbn())) {
                return;
            }
        }

        this.bookList.add(book);
    }

    @Override
    public Book getBookByIsbn(String s) {
        for (Book book : this.bookList){
            if(book.getIsbn().equals(s)){
                return book;
            }
        }
        throw new BookNotFoundException("Book not found for ISBN " + s);
    }

    @Override
    public List<Book> searchBooksByAuthor(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Author name should not be null or empty");
        }
        List<Book> booksByAuthor = new ArrayList<>();
        for (Book book : this.bookList) {
            for (Author author : book.getAuthors()) {
                if (author.getName().equals(s)) {
                    booksByAuthor.add(book);
                }
            }
        }
        return booksByAuthor;
    }

    @Override
    public int getTotalNumberOfBooks() {
        return bookList.size();
    }

    @Override
    public boolean isBookInCatalog(String s) {
        for (Book book : this.bookList) {
            if (book.getIsbn().equals(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBookAvailable(String s) {
        for (Book book : this.bookList) {
            if (book.getIsbn().equals(s)) {
                return book.isAvailable();
            }
        }
        throw new BookNotFoundException("Book not found for ISBN " + s);
    }

    @Override
    public Book findNewestBookByPublisher(String s) {
        return this.bookList.stream()
                .filter(book -> book.getPublisher().equals(s))
                .max(Comparator.comparingInt(Book::getPublicationYear))
                .orElseThrow(() -> new BookNotFoundException("No book found for publisher " + s));
    }

    @Override
    public List<Book> getSortedBooks() {
        return this.bookList.stream()
                .sorted(Comparator.comparingInt(Book::getPublicationYear).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Book>> groupBooksByPublisher() {
        return this.bookList.stream().collect(Collectors.groupingBy(Book::getPublisher));
    }

    @Override
    public List<Book> filterBooks(Predicate<Book> predicate) {
        return this.bookList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateTotalPrice() {
        return this.bookList.stream()
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public List<Book> filterBooksUsingPredicate(Predicate<Book> predicate) {
        return this.bookList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(String searchText) {
        return this.bookList.stream()
                .filter(book -> book.getTitle().contains(searchText) ||
                        book.getPublisher().contains(searchText) ||
                        book.getAuthors().stream().anyMatch(author -> author.getName().contains(searchText))
                )
                .collect(Collectors.toList());
    }


}
