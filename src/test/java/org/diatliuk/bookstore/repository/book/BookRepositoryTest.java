package org.diatliuk.bookstore.repository.book;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final String BEFORE_TEST_METHOD_QUERIES = "classpath:database"
                                                + "/queries/books/add-books-and-categories.sql";
    private static final String AFTER_TEST_METHOD_QUERIES = "classpath:database"
                                                + "/queries/books/delete-books-and-categories.sql";
    
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll() {

    }

    @Test
    @DisplayName("Verify the findById() method by using an existing id")
    @Sql(
            scripts = BEFORE_TEST_METHOD_QUERIES,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = AFTER_TEST_METHOD_QUERIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findById_withValidId_returnsBook() {
    }

    @Test
    @DisplayName("Verify the findById() method by using not existing book id")
    @Sql(
            scripts = BEFORE_TEST_METHOD_QUERIES,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = AFTER_TEST_METHOD_QUERIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findById_withNotExistingBookId_returnsNull() {

    }

    @Test
    @DisplayName("Verify the findAll() method")
    @Sql(
            scripts = BEFORE_TEST_METHOD_QUERIES,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = AFTER_TEST_METHOD_QUERIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findAll_returnsAllBooks() {

    }
}
