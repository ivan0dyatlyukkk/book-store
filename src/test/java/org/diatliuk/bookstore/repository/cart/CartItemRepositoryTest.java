package org.diatliuk.bookstore.repository.cart;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.diatliuk.bookstore.model.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {
    private static final String ADD_DEFAULT_USERS_AND_CARTS = "classpath:"
            + "database/queries/carts/add-default-users-and-carts.sql";
    private static final String DELETE_DEFAULT_USERS_AND_CARTS = "classpath:"
            + "database/queries/carts/delete-default-users-and-carts.sql";
    private static final Long TEST_VALID_CART_ITEM_ID = 1L;
    private static final Long TEST_VALID_CART_ID = 1L;

    @Autowired
    private CartItemRepository cartItemRepository;

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
    }
}
