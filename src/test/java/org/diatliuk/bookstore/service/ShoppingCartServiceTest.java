package org.diatliuk.bookstore.service;

import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.mapper.CartItemMapper;
import org.diatliuk.bookstore.mapper.ShoppingCartMapper;
import org.diatliuk.bookstore.model.*;
import org.diatliuk.bookstore.repository.book.BookRepository;
import org.diatliuk.bookstore.repository.cart.CartItemRepository;
import org.diatliuk.bookstore.repository.cart.ShoppingCartRepository;
import org.diatliuk.bookstore.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    private static final CreateCartItemRequestDto CART_ITEM_REQUEST_DTO = new CreateCartItemRequestDto();
    private static final CartItemDto CART_ITEM_DTO = new CartItemDto();
    private static final Book TEST_BOOK = new Book();
    private static final User TEST_USER = new User();
    private static final CartItem TEST_CART_ITEM = new CartItem();
    private static final ShoppingCart TEST_SHOPPING_CART = new ShoppingCart();

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    static void beforeAll() {
        CART_ITEM_REQUEST_DTO.setBookId(1L);
        CART_ITEM_REQUEST_DTO.setQuantity(5);

        CART_ITEM_DTO.setId(1L);
        CART_ITEM_DTO.setBookId(CART_ITEM_REQUEST_DTO.getBookId());
        CART_ITEM_DTO.setBookTitle("TEST");
        CART_ITEM_DTO.setQuantity(CART_ITEM_REQUEST_DTO.getQuantity());

        TEST_BOOK.setId(1L);
        TEST_BOOK.setTitle("Test book 1");
        TEST_BOOK.setAuthor("Tester 1");
        TEST_BOOK.setIsbn("978-0-06-251140-9");
        TEST_BOOK.setPrice(BigDecimal.valueOf(200));
        TEST_BOOK.setDescription("This is a description");
        TEST_BOOK.setCoverImage("link.ua//image1");
        TEST_BOOK.setCategories(Set.of());

        TEST_USER.setId(1L);
        TEST_USER.setEmail("test@gmail.com");
        TEST_USER.setPassword("test");
        TEST_USER.setFirstName("user");
        TEST_USER.setLastName("tester");
        TEST_USER.setRoles(Set.of());

        TEST_CART_ITEM.setId(1L);
        TEST_CART_ITEM.setBook(TEST_BOOK);
        TEST_CART_ITEM.setQuantity(1);
        TEST_CART_ITEM.setShoppingCart(TEST_SHOPPING_CART);

        TEST_SHOPPING_CART.setId(1L);
        TEST_SHOPPING_CART.setUser(TEST_USER);
        TEST_SHOPPING_CART.setCartItems(Set.of(TEST_CART_ITEM));
    }

    @Test
    @DisplayName("Verify the save() method by using valid data")
    void save_withValidData_returnCartItem() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(TEST_BOOK));
        when(userService.getAuthenticatedUser(any())).thenReturn(TEST_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(TEST_USER.getId())).thenReturn(TEST_SHOPPING_CART);

    }
}