package org.diatliuk.bookstore.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.diatliuk.bookstore.dto.book.BookDto;
import org.diatliuk.bookstore.dto.category.CategoryResponseDto;
import org.diatliuk.bookstore.model.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final String ADD_DEFAULT_BOOKS_AND_CATEGORIES = "classpath:database"
            + "/queries/books/add-books-and-categories.sql";
    private static final String DELETE_DEFAULT_BOOKS_AND_CATEGORIES = "classpath:database"
                                                + "/queries/books/delete-books-and-categories.sql";
    private static final List<CategoryResponseDto> DEFAULT_CATEGORIES = List.of(
            new CategoryResponseDto(1L, "Fiction", "Fiction books"),
            new CategoryResponseDto(2L, "Classic Literature", "Timeless literary masterpieces")
    );
    private static final int FIST_ELEMENT = 0;
    private static final int TEST_CATEGORY_INDEX = 1;
    private static final List<BookDto> DEFAULT_BOOKS = List.of(
            new BookDto(1L, "Test book #1", "Bob Bobenko",
                    "978-0-16-251140-9", BigDecimal.valueOf(200), "This is a description",
                    "link.ua//images/1", Set.of(1L)),
            new BookDto(2L, "Test book #2", "Ivan Ivanenko",
                    "978-0-16-251141-9", BigDecimal.valueOf(100), "This is a description",
                    "link.ua//images/2", Set.of(2L)),
            new BookDto(3L, "Test book #3", "Bob Bobenko",
                    "978-0-16-261150-9", BigDecimal.valueOf(150), "This is a description",
                    "link.ua//images/3", Set.of(2L))
    );
    private static final Book TEST_BOOK = new Book();

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll() {
        TEST_BOOK.setTitle("TEST BOOK");
        TEST_BOOK.setAuthor("TESTER");
        TEST_BOOK.setIsbn("978-0-16-261150-7");
        TEST_BOOK.setPrice(BigDecimal.valueOf(100));
        TEST_BOOK.setDescription("This is a test book");
        TEST_BOOK.setCoverImage("link");
        TEST_BOOK.setCategories(Set.of());
        TEST_BOOK.setDeleted(false);
    }

    @Test
    @DisplayName("Verify the findAllByCategoriesId() method by using an existing id")
    @Sql(
            scripts = ADD_DEFAULT_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_DEFAULT_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findAllByCategoriesId_withValidId_returnsBooks() {
        Long categoryId = DEFAULT_CATEGORIES.get(TEST_CATEGORY_INDEX).getId();
        long expectedNumber = DEFAULT_BOOKS.stream()
                .filter(bookDto -> bookDto.getCategoryIds().contains(categoryId)).count();

        List<Book> actualBooks = bookRepository.findAllByCategoriesId(categoryId);

        assertEquals(expectedNumber, actualBooks.size());
        assertNotNull(actualBooks.get(FIST_ELEMENT).getCategories());
    }

    @Test
    @DisplayName("Verify the findById() method by using an existing id")
    @Sql(
            scripts = ADD_DEFAULT_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_DEFAULT_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findById_withValidId_returnsBook() {
        Book expectedBook = bookRepository.save(TEST_BOOK);

        Book actualBook = bookRepository.findById(expectedBook.getId()).get();

        assertNotNull(actualBook.getCategories());
        assertTrue(EqualsBuilder.reflectionEquals(expectedBook, actualBook, "id"));
    }

    @Test
    @DisplayName("Verify the findAll() method")
    @Sql(
            scripts = ADD_DEFAULT_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_DEFAULT_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findAll_returnsAllBooks() {
        int expectedNumber = DEFAULT_BOOKS.size();
        Pageable pageable = Pageable.ofSize(10);

        Page<Book> actualAllBooks = bookRepository.findAll(pageable);

        assertEquals(expectedNumber, actualAllBooks.getContent().size());
    }
}
