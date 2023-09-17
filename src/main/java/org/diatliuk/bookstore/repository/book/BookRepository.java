package org.diatliuk.bookstore.repository.book;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.diatliuk.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByCategoriesId(Long categoryId);

    @NotNull
    @Override
    @Query("FROM Book b LEFT JOIN FETCH b.categories WHERE b.id = ?1")
    Optional<Book> findById(@NotNull Long id);

    @NotNull
    @Override
    @Query("FROM Book b LEFT JOIN FETCH b.categories")
    Page<Book> findAll(@NotNull Pageable pageable);

    @NotNull
    @Query("FROM Book b LEFT JOIN FETCH b.categories")
    List<Book> findAll(@NotNull Specification<Book> specification);
}
