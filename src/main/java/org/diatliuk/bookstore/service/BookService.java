package org.diatliuk.bookstore.service;

import java.util.List;
import org.diatliuk.bookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
