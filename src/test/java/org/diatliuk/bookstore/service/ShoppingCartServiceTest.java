package org.diatliuk.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.dto.cart.item.UpdateCartItemDto;
import org.diatliuk.bookstore.exception.IllegalUserAccessException;
import org.diatliuk.bookstore.mapper.CartItemMapper;
import org.diatliuk.bookstore.mapper.ShoppingCartMapper;
import org.diatliuk.bookstore.model.Book;
import org.diatliuk.bookstore.model.CartItem;
import org.diatliuk.bookstore.model.ShoppingCart;
import org.diatliuk.bookstore.model.User;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    private static final CreateCartItemRequestDto CART_ITEM_REQUEST_DTO =
                                            new CreateCartItemRequestDto();
    private static final CartItemDto CART_ITEM_DTO = new CartItemDto();
    private static final Book TEST_BOOK = new Book();
    private static final User TEST_USER = new User();
    private static final CartItem TEST_CART_ITEM = new CartItem();
    private static final UpdateCartItemDto UPDATE_CART_ITEM_DTO = new UpdateCartItemDto();
    private static final ShoppingCart TEST_SHOPPING_CART = new ShoppingCart();
    private static final ShoppingCartDto TEST_SHOPPING_CART_DTO = new ShoppingCartDto();
    private static final String ERROR_MESSAGE = "The user can't modify this cart item!";
    private static final Long INVALID_CART_ITEM_ID = -1L;

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
        TEST_BOOK.setId(1L);
        TEST_BOOK.setTitle("Test book 1");
        TEST_BOOK.setAuthor("Tester 1");
        TEST_BOOK.setIsbn("978-0-06-251140-9");
        TEST_BOOK.setPrice(BigDecimal.valueOf(200));
        TEST_BOOK.setDescription("This is a description");
        TEST_BOOK.setCoverImage("link.ua//image1");
        TEST_BOOK.setCategories(Set.of());

        CART_ITEM_REQUEST_DTO.setBookId(TEST_BOOK.getId());
        CART_ITEM_REQUEST_DTO.setQuantity(5);

        CART_ITEM_DTO.setId(1L);
        CART_ITEM_DTO.setBookId(CART_ITEM_REQUEST_DTO.getBookId());
        CART_ITEM_DTO.setBookTitle(TEST_BOOK.getTitle());
        CART_ITEM_DTO.setQuantity(CART_ITEM_REQUEST_DTO.getQuantity());

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

        UPDATE_CART_ITEM_DTO.setQuantity(TEST_CART_ITEM.getQuantity());

        TEST_SHOPPING_CART.setId(1L);
        TEST_SHOPPING_CART.setUser(TEST_USER);
        TEST_SHOPPING_CART.setCartItems(Set.of(TEST_CART_ITEM));

        TEST_SHOPPING_CART_DTO.setId(TEST_SHOPPING_CART.getId());
        TEST_SHOPPING_CART_DTO.setUserId(TEST_SHOPPING_CART.getUser().getId());
        TEST_SHOPPING_CART_DTO.setCartItems(Set.of(CART_ITEM_DTO));
    }

    @Test
    @DisplayName("Verify the save() method by using valid data")
    void save_withValidData_returnCartItem() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(TEST_BOOK));
        when(userService.getAuthenticatedUser(any())).thenReturn(TEST_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(TEST_USER.getId()))
                                                .thenReturn(TEST_SHOPPING_CART);
        when(cartItemRepository.save(any())).thenReturn(TEST_CART_ITEM);
        when(cartItemMapper.toDto(TEST_CART_ITEM)).thenReturn(CART_ITEM_DTO);

        CartItemDto actualCart = shoppingCartService.save(any(), CART_ITEM_REQUEST_DTO);

        assertEquals(CART_ITEM_DTO, actualCart);
    }

    @Test
    @DisplayName("Verify the get() method")
    void get_returnsShoppingCart() {
        when(userService.getAuthenticatedUser(any())).thenReturn(TEST_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(TEST_USER.getId()))
                                                .thenReturn(TEST_SHOPPING_CART);
        when(cartItemRepository.findAllByShoppingCartId(TEST_SHOPPING_CART.getId()))
                                                .thenReturn(List.of(TEST_CART_ITEM));
        when(shoppingCartMapper.toDto(TEST_SHOPPING_CART))
                        .thenReturn(TEST_SHOPPING_CART_DTO);

        ShoppingCartDto actualShoppingCartDto = shoppingCartService.get(any());

        assertEquals(TEST_SHOPPING_CART_DTO, actualShoppingCartDto);
    }

    @Test
    @DisplayName("Verify the update() method by using valid data")
    void update_withValidData_returnsCartItem() {
        when(userService.getAuthenticatedUser(any())).thenReturn(TEST_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(TEST_USER.getId()))
                                                .thenReturn(TEST_SHOPPING_CART);
        when(cartItemRepository.findById(TEST_CART_ITEM.getId()))
                        .thenReturn(Optional.of(TEST_CART_ITEM));
        when(cartItemRepository.save(TEST_CART_ITEM))
                            .thenReturn(TEST_CART_ITEM);
        when(cartItemMapper.toDto(TEST_CART_ITEM))
                             .thenReturn(CART_ITEM_DTO);

        CartItemDto updatedCartItem = shoppingCartService.update(any(),
                                                                TEST_CART_ITEM.getId(),
                                                                UPDATE_CART_ITEM_DTO);

        assertEquals(CART_ITEM_DTO, updatedCartItem);
    }

    @Test
    @DisplayName("Verify the deleteById() method by using an existing id")
    void deleteById_withValidId_void() {
        when(userService.getAuthenticatedUser(any())).thenReturn(TEST_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(TEST_BOOK.getId()))
                                                .thenReturn(TEST_SHOPPING_CART);

        assertDoesNotThrow(
                () -> shoppingCartService.deleteById(any(), TEST_CART_ITEM.getId())
        );
    }

    @Test
    @DisplayName("Verify the deleteById() method by using not existing id")
    void deleteById_withInvalid_throwsException() {
        when(userService.getAuthenticatedUser(any())).thenReturn(TEST_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(TEST_BOOK.getId()))
                                                .thenReturn(TEST_SHOPPING_CART);

        Exception exception = assertThrows(IllegalUserAccessException.class,
                () -> shoppingCartService.deleteById(any(), INVALID_CART_ITEM_ID)
        );

        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Verify the create() method by using a valid user")
    void create_withValidUser_returnsShoppingCart() {
        ShoppingCart expected = new ShoppingCart();
        expected.setUser(TEST_USER);
        when(shoppingCartRepository.save(any())).thenReturn(expected);

        ShoppingCart actualShoppingCart = shoppingCartService.create(TEST_USER);

        assertTrue(EqualsBuilder.reflectionEquals(expected, actualShoppingCart, "id"));
        assertEquals(TEST_USER, actualShoppingCart.getUser());
    }
}
