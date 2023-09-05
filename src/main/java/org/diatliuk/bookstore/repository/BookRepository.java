package org.diatliuk.bookstore.repository;

import java.util.List;
import java.util.Optional;
import org.diatliuk.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findBookById(Long id);

    List<Book> findAll();
}
