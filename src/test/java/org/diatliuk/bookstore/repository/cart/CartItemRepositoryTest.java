package org.diatliuk.bookstore.repository.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.model.CartItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {
    private static final String ADD_DEFAULT_USERS_AND_CARTS = "classpath:"
            + "database/queries/carts/add-default-users-and-carts.sql";
    private static final String DELETE_DEFAULT_USERS_AND_CARTS = "classpath:"
            + "database/queries/carts/delete-default-users-and-carts.sql";
    private static final Book DEFAULT_BOOK_OF_CART_ITEM = new Book();

    private static final Long TEST_VALID_CART_ITEM_ID = 1L;
    private static final Long TEST_VALID_CART_ID = 1L;

    @Autowired
    private CartItemRepository cartItemRepository;

    @BeforeAll
    static void beforeAll() {
        DEFAULT_BOOK_OF_CART_ITEM.setId(4L);
        DEFAULT_BOOK_OF_CART_ITEM.setTitle("Test");
        DEFAULT_BOOK_OF_CART_ITEM.setAuthor("ADMIN ADMIN");
        DEFAULT_BOOK_OF_CART_ITEM.setIsbn("978-161-729-045-9");
        DEFAULT_BOOK_OF_CART_ITEM.setPrice(BigDecimal.valueOf(200));
        DEFAULT_BOOK_OF_CART_ITEM.setDescription("This is a description");
        DEFAULT_BOOK_OF_CART_ITEM.setCoverImage("link.ua//images/1");
        DEFAULT_BOOK_OF_CART_ITEM.setDeleted(false);
    }

    @Test
    @DisplayName("Verify the findById() method by using an existing id")
    @Sql(
            scripts = ADD_DEFAULT_USERS_AND_CARTS,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_DEFAULT_USERS_AND_CARTS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findById_withValidId_returnsCartItem() {
        CartItem actualCartItem = cartItemRepository.findById(TEST_VALID_CART_ITEM_ID).get();

        assertNotNull(actualCartItem.getShoppingCart());
    }

    @Test
    @DisplayName("Verify the findAllByShoppingCartId() method by using an existing id")
    @Sql(
            scripts = ADD_DEFAULT_USERS_AND_CARTS,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_DEFAULT_USERS_AND_CARTS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findAllByShoppingCardId_withValidId_returnsCartItems() {
        CartItem actualCartItem = cartItemRepository
                                .findAllByShoppingCartId(TEST_VALID_CART_ITEM_ID).get(0);

        assertNotNull(actualCartItem);
        assertNotNull(actualCartItem.getShoppingCart());
        assertEquals(TEST_VALID_CART_ID, actualCartItem.getShoppingCart().getId());
        assertTrue(EqualsBuilder.reflectionEquals(DEFAULT_BOOK_OF_CART_ITEM,
                                                  actualCartItem.getBook()));
    }
}
