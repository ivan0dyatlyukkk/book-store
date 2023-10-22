package org.diatliuk.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.diatliuk.bookstore.dto.book.BookDto;
import org.diatliuk.bookstore.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    private static final String ADD_DEFAULT_BOOKS_AND_CATEGORIES = "database"
            + "/queries/books/add-books-and-categories.sql";
    private static final String DELETE_ALL_BOOKS_AND_CATEGORIES = "database"
            + "/queries/books/delete-books-and-categories.sql";
    private static final String ADD_TEST_BOOK = "classpath:"
            + "database/queries/books/add-test-book.sql";
    private static final Long TEST_BOOK_ID = 4L;
    private static final String DELETE_TEST_BOOK = "classpath:"
                            + "database/queries/books/delete-test-book.sql";
    private static final CreateBookRequestDto VALID_BOOK_REQUEST_DTO = new CreateBookRequestDto();
    private static final CreateBookRequestDto BOOK_UPDATE_REQUEST_DTO = new CreateBookRequestDto();
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
    private static final Long INVALID_BOOK_ID = -100L;
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(ADD_DEFAULT_BOOKS_AND_CATEGORIES)
            );
        }

        VALID_BOOK_REQUEST_DTO.setTitle("Test");
        VALID_BOOK_REQUEST_DTO.setAuthor("Tester");
        VALID_BOOK_REQUEST_DTO.setIsbn("978-161-729-045-9");
        VALID_BOOK_REQUEST_DTO.setPrice(BigDecimal.valueOf(200));
        VALID_BOOK_REQUEST_DTO.setDescription("This is a description");
        VALID_BOOK_REQUEST_DTO.setCoverImage("link.ua/images/1");
        VALID_BOOK_REQUEST_DTO.setCategoryIds(Set.of());

        BOOK_UPDATE_REQUEST_DTO.setTitle(VALID_BOOK_REQUEST_DTO.getTitle());
        BOOK_UPDATE_REQUEST_DTO.setAuthor("Ivan Diatliuk");
        BOOK_UPDATE_REQUEST_DTO.setIsbn(VALID_BOOK_REQUEST_DTO.getIsbn());
        BOOK_UPDATE_REQUEST_DTO.setPrice(BigDecimal.valueOf(1000));
        BOOK_UPDATE_REQUEST_DTO.setDescription(VALID_BOOK_REQUEST_DTO.getDescription());
        BOOK_UPDATE_REQUEST_DTO.setCoverImage(VALID_BOOK_REQUEST_DTO.getCoverImage());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify the create() method by using valid request dto data")
    @Sql(
            scripts = DELETE_TEST_BOOK,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void create_withValidCreateBookRequest_returnsBook() throws Exception {
        BookDto expectedBook = new BookDto()
                .setId(1L)
                .setTitle(VALID_BOOK_REQUEST_DTO.getTitle())
                .setAuthor(VALID_BOOK_REQUEST_DTO.getAuthor())
                .setIsbn(VALID_BOOK_REQUEST_DTO.getIsbn())
                .setPrice(VALID_BOOK_REQUEST_DTO.getPrice())
                .setDescription(VALID_BOOK_REQUEST_DTO.getDescription())
                .setCoverImage(VALID_BOOK_REQUEST_DTO.getCoverImage())
                .setCategoryIds(VALID_BOOK_REQUEST_DTO.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(VALID_BOOK_REQUEST_DTO);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        BookDto actualBook = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                    BookDto.class);

        assertNotNull(actualBook);
        assertNotNull(actualBook.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expectedBook, actualBook, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify the getAll() method")
    void getAll_returnsBooks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto[] actualBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), BookDto[].class
        );
        assertEquals(DEFAULT_BOOKS.size(), actualBooks.length);
        assertEquals(DEFAULT_BOOKS, Arrays.stream(actualBooks).toList());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify the getById() method by using an existing id")
    void getById_withValidId_returnsBook() throws Exception {
        BookDto expected = DEFAULT_BOOKS.get(0);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{id}", expected.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto actualBook = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto.class
        );

        assertNotNull(actualBook);
        assertNotNull(actualBook.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expected, actualBook));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify the getById() method by using not existing id")
    void getById_withInvalidId_returnsErrorCode() throws Exception {
        ResultMatcher expectedStatus = MockMvcResultMatchers.status().isNotFound();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{id}", INVALID_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(expectedStatus);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify the update() method by using valid book id and data")
    @Sql(
            scripts = ADD_TEST_BOOK,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_TEST_BOOK,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void update_withValidIdAndData_returnsBook() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(BOOK_UPDATE_REQUEST_DTO);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/{id}", TEST_BOOK_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto updatedBook = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto.class
        );

        assertNotNull(updatedBook);
        assertEquals(BOOK_UPDATE_REQUEST_DTO.getAuthor(), updatedBook.getAuthor());
        assertEquals(BOOK_UPDATE_REQUEST_DTO.getPrice(), updatedBook.getPrice());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify the deleteById() method by using an existing id")
    @Sql(
            scripts = ADD_TEST_BOOK,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void deleteById_withValidId_void() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/{id}", TEST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        BookDto[] actualBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), BookDto[].class
        );
        assertEquals(DEFAULT_BOOKS.size(), actualBooks.length);
        assertEquals(DEFAULT_BOOKS, Arrays.stream(actualBooks).toList());
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        tearDown(dataSource);
    }

    @SneakyThrows
    static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(DELETE_ALL_BOOKS_AND_CATEGORIES)
            );
        }
    }
}
