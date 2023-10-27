package org.diatliuk.bookstore.repository.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.diatliuk.bookstore.model.ShoppingCart;
import org.diatliuk.bookstore.model.User;
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
class ShoppingCartRepositoryTest {
    private static final String ADD_DEFAULT_USERS_AND_CARTS = "classpath:"
            + "database/queries/carts/add-default-users-and-carts.sql";
    private static final String DELETE_DEFAULT_USERS_AND_CARTS = "classpath:"
            + "database/queries/carts/delete-default-users-and-carts.sql";
    private static final Long TEST_VALID_USER_ID = 4L;
    private static final int DEFAULT_NUMBER_OF_ITEMS = 1;
    private static final User DEFAULT_USER = new User();

    @Autowired
    private ShoppingCartRepository cartRepository;

    @BeforeAll
    static void beforeAll() {
        DEFAULT_USER.setId(4L);
        DEFAULT_USER.setEmail("test@gmail.com");
        DEFAULT_USER.setPassword("test0test");
        DEFAULT_USER.setFirstName("test");
        DEFAULT_USER.setLastName("tester");
        DEFAULT_USER.setShippingAddress("shippingAddress");
        DEFAULT_USER.setDeleted(false);
    }

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

        assertNotNull(actualCart.getCartItems());
        assertEquals(DEFAULT_NUMBER_OF_ITEMS, actualCart.getCartItems().size());
        assertTrue(EqualsBuilder.reflectionEquals(DEFAULT_USER, actualCart.getUser(), "roles"));
    }
}
