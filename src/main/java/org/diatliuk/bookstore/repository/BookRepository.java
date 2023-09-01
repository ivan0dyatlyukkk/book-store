package org.diatliuk.bookstore.repository;

import java.util.List;
import org.diatliuk.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
