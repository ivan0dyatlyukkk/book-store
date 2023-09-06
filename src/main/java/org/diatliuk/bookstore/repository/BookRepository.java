package org.diatliuk.bookstore.repository;

import org.diatliuk.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
