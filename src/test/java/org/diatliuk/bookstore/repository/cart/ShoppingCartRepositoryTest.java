package org.diatliuk.bookstore.repository.cart;

import org.diatliuk.bookstore.model.ShoppingCart;
import org.diatliuk.bookstore.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    private static final String ADD_DEFAULT_USERS_AND_CARTS = "classpath:"
            + "database/queries/carts/add-default-users-and-carts.sql";
    private static final String DELETE_DEFAULT_USERS_AND_CARTS = "classpath:"
            + "database/queries/carts/delete-default-users-and-carts.sql";
    private static final Long TEST_VALID_USER_ID = 4L;

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Test
    @DisplayName("Verify the getShoppingCartByUserId() method by using an existing id")
    @Sql(
            scripts = ADD_DEFAULT_USERS_AND_CARTS,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_DEFAULT_USERS_AND_CARTS,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getShoppingCartByUserId_withValidUserId_returnsCart() {
        ShoppingCart actualCart = cartRepository.getShoppingCartByUserId(TEST_VALID_USER_ID);

        assertNotNull(actualCart.getUser());
        assertNotNull(actualCart.getCartItems());
    }
}