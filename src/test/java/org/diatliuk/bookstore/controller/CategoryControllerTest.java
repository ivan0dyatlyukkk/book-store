package org.diatliuk.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.diatliuk.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.diatliuk.bookstore.dto.category.CategoryDto;
import org.diatliuk.bookstore.dto.category.CategoryResponseDto;
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
class CategoryControllerTest {
    private static final String ADD_DEFAULT_BOOKS_AND_CATEGORIES = "database"
            + "/queries/books/add-books-and-categories.sql";
    private static final String DELETE_ALL_BOOKS_AND_CATEGORIES = "database"
            + "/queries/books/delete-books-and-categories.sql";
    private static final String DELETE_TEST_CATEGORY = "classpath:"
            + "database/queries/categories/delete-test-category.sql";
    private static final String ADD_TEST_CATEGORY = "classpath:"
            + "database/queries/categories/add-test-category.sql";
    private static final Long TEST_CATEGORY_ID = 3L;
    private static final CategoryDto TEST_CATEGORY_DTO = new CategoryDto();
    private static final CategoryDto CATEGORY_UPDATE_DTO = new CategoryDto();
    private static final Long INVALID_CATEGORY_ID = -1L;
    private static final List<CategoryResponseDto> DEFAULT_CATEGORIES = List.of(
            new CategoryResponseDto(1L, "Fiction", "Fiction books"),
            new CategoryResponseDto(2L, "Classic Literature", "Timeless literary masterpieces")
    );
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

        TEST_CATEGORY_DTO.setName("Test category");
        TEST_CATEGORY_DTO.setDescription("Test category books");

        CATEGORY_UPDATE_DTO.setName(TEST_CATEGORY_DTO.getName());
        CATEGORY_UPDATE_DTO.setDescription("This is an updated description");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify the create() method by using valid data")
    @Sql(
            scripts = DELETE_TEST_CATEGORY,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void create_withValidData_returnsCategory() throws Exception {
        CategoryResponseDto expectedCategory = new CategoryResponseDto()
                .setId(1L)
                .setName(TEST_CATEGORY_DTO.getName())
                .setDescription(TEST_CATEGORY_DTO.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(TEST_CATEGORY_DTO);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        CategoryResponseDto actualCategory = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryResponseDto.class
        );

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expectedCategory, actualCategory, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify the getAll() method")
    void getAll_returnsCategories() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryResponseDto[] actualCategories = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), CategoryResponseDto[].class
        );
        assertEquals(DEFAULT_CATEGORIES.size(), actualCategories.length);
        assertEquals(DEFAULT_CATEGORIES, Arrays.stream(actualCategories).toList());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify the getById() method by using an existing id")
    void getById_withValidId_returnsCategory() throws Exception {
        CategoryDto expectedCategory = new CategoryDto()
                .setName(DEFAULT_CATEGORIES.get(0).getName())
                .setDescription(DEFAULT_CATEGORIES.get(0).getDescription());

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/{id}",
                                                            DEFAULT_CATEGORIES.get(0).getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto actualCategory = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class
        );

        assertNotNull(actualCategory);
        assertTrue(EqualsBuilder.reflectionEquals(expectedCategory, actualCategory));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify the getById() method by using not existing id")
    void getById_withInvalidId_throwsException() throws Exception {
        ResultMatcher expectedStatus = MockMvcResultMatchers.status().isNotFound();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/{id}", INVALID_CATEGORY_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(expectedStatus);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify the update() method by using valid id and data")
    @Sql(
            scripts = ADD_TEST_CATEGORY,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_TEST_CATEGORY,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void update_withValidIdAndData_returnsCategory() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(CATEGORY_UPDATE_DTO);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/categories/{id}", TEST_CATEGORY_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto updatedCategory = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class
        );

        assertNotNull(updatedCategory);
        assertEquals(CATEGORY_UPDATE_DTO.getDescription(), updatedCategory.getDescription());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify the deleteById() method by using an existing id")
    @Sql(
            scripts = ADD_TEST_CATEGORY,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void deleteById_withValidId_void() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/categories/{id}", TEST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        CategoryDto[] actualCategories = objectMapper.readValue(
            mvcResult.getResponse().getContentAsByteArray(), CategoryDto[].class
        );
        assertEquals(DEFAULT_CATEGORIES.size(), actualCategories.length);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Verify the getBooksByCategoryId() method by using an existing id")
    void getBooksByCategoryId_withValidId_returnsBooks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/{id}/books",
                                                            DEFAULT_CATEGORIES.get(1).getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds[] actualBooks = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(), BookDtoWithoutCategoryIds[].class
        );

        assertNotNull(actualBooks);
        assertNotEquals(0, actualBooks.length);
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
