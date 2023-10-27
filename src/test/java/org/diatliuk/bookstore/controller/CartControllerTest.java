package org.diatliuk.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import javax.sql.DataSource;
import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.dto.cart.item.UpdateCartItemDto;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartControllerTest {
    private static final String ADD_DEFAULT_DATA = "database"
            + "/queries/carts/add-default-users-and-carts.sql";
    private static final String DELETE_ALL_DATA = "database"
            + "/queries/carts/delete-default-users-and-carts.sql";
    private static final User TEST_USER = new User();
    private static final String TEST_USER_NAME = "test@gmail.com";
    private static final Book TEST_BOOK = new Book();
    private static final int TEST_BOOK_QUANTITY = 5;
    private static final CartItemDto TEST_CART_ITEM_DTO = new CartItemDto();
    private static final CartItemDto DEFAULT_CART_ITEM_DTO = new CartItemDto();
    private static final int UPDATED_CART_ITEM_QUANTITY = 10;
    private static final Long DEFAULT_CART_ITEM_ID = 1L;
    private static final String DEFAULT_BOOK_TITLE = "Test";
    private static final int DEFAULT_CART_ITEM_QUANTITY = 1;

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        TEST_USER.setId(4L);
        TEST_USER.setEmail("test@gmail.com'");
        TEST_USER.setFirstName("test");
        TEST_USER.setLastName("tester");
        TEST_USER.setDeleted(false);
        TEST_USER.setPassword("test0test");
        TEST_USER.setRoles(new HashSet<>());
        TEST_USER.setShippingAddress("shippingAddress");

        TEST_BOOK.setId(5L);
        TEST_BOOK.setTitle("The book 5");
        TEST_BOOK.setAuthor("ADMIN ADMIN");

        TEST_CART_ITEM_DTO.setId(1L);
        TEST_CART_ITEM_DTO.setBookId(TEST_BOOK.getId());
        TEST_CART_ITEM_DTO.setBookTitle(TEST_BOOK.getTitle());
        TEST_CART_ITEM_DTO.setQuantity(TEST_BOOK_QUANTITY);

        DEFAULT_CART_ITEM_DTO.setId(1L);
        DEFAULT_CART_ITEM_DTO.setBookId(4L);
        DEFAULT_CART_ITEM_DTO.setBookTitle("Test");
        DEFAULT_CART_ITEM_DTO.setQuantity(1);
    }

    @BeforeEach
    void setUp(
            @Autowired DataSource dataSource
    ) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(ADD_DEFAULT_DATA)
            );
        }
    }

    @WithMockUser(username = TEST_USER_NAME)
    @Test
    @DisplayName("Verify the saveItem() method by using valid data")
    void saveItem_withValidData_returnsCartItem() throws Exception {
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto()
                .setBookId(TEST_BOOK.getId())
                .setQuantity(TEST_BOOK_QUANTITY);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CartItemDto actualCartItem = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CartItemDto.class
        );

        assertNotNull(actualCartItem);
        assertNotNull(actualCartItem.getId());
        assertTrue(EqualsBuilder.reflectionEquals(TEST_CART_ITEM_DTO, actualCartItem, "id"));
    }

    @WithMockUser(username = TEST_USER_NAME)
    @Test
    @DisplayName("Verify the get() method")
    void get_returnsShoppingCart() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ShoppingCartDto shoppingCartDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartDto.class
        );

        assertNotNull(shoppingCartDto);
        assertEquals(TEST_USER.getId(), shoppingCartDto.getUserId());
        assertTrue(shoppingCartDto.getCartItems().contains(DEFAULT_CART_ITEM_DTO));
    }

    @WithMockUser(username = TEST_USER_NAME)
    @Test
    @DisplayName("Verify the updateItemQuantity() method by using valid data")
    void updateItemQuantity_withValidData_returnsCartItem() throws Exception {
        UpdateCartItemDto updateRequest = new UpdateCartItemDto()
                .setQuantity(UPDATED_CART_ITEM_QUANTITY);

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/cart/cart-items/{cartItemId}",
                                                            DEFAULT_CART_ITEM_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CartItemDto actualCartItem = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CartItemDto.class
        );

        assertNotNull(actualCartItem);
        assertEquals(UPDATED_CART_ITEM_QUANTITY, actualCartItem.getQuantity());
        assertEquals(DEFAULT_BOOK_TITLE, actualCartItem.getBookTitle());
    }

    @WithMockUser(username = TEST_USER_NAME)
    @Test
    @DisplayName("Verify the deleteItemById() method by using an existing id")
    void deleteItemById_withValidId_void() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/cart/cart-items/{cartItemId}",
                                                                DEFAULT_CART_ITEM_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ShoppingCartDto shoppingCartDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartDto.class
        );

        assertEquals(DEFAULT_CART_ITEM_QUANTITY - 1, shoppingCartDto.getCartItems().size());
    }

    @AfterEach
    void tearDown(
            @Autowired DataSource dataSource
    ) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(DELETE_ALL_DATA)
            );
        }
    }
}
