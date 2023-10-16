package org.diatliuk.bookstore.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.dto.cart.item.UpdateCartItemDto;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
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
import org.diatliuk.bookstore.service.ShoppingCartService;
import org.diatliuk.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    @Lazy
    @Autowired
    private UserService userService;

    @Override
    public ShoppingCartDto get(Authentication authentication) {
        User authenticatedUser = userService.getAuthenticatedUser(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository
                                    .getShoppingCartByUserId(authenticatedUser.getId());
        List<CartItem> cartItems = cartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
        shoppingCart.setCartItems(Set.copyOf(cartItems));
        return cartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public CartItemDto save(Authentication authentication, CreateCartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id + "
                        + requestDto.getBookId()));
        cartItem.setBook(book);

        User authenticatedUser = userService.getAuthenticatedUser(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(authenticatedUser.getId());

        if (isBookPresent(shoppingCart, requestDto.getBookId())) {
            CartItem existingCartItem = shoppingCart.getCartItems()
                    .stream()
                    .filter(item -> item.getBook().getId().equals(requestDto.getBookId()))
                    .findAny()
                    .get();

            cartItem.setId(existingCartItem.getId());
            cartItem.setQuantity(existingCartItem.getQuantity() + requestDto.getQuantity());
        } else {
            cartItem.setQuantity(requestDto.getQuantity());
        }

        cartItem.setShoppingCart(shoppingCart);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Transactional
    @Override
    public CartItemDto update(Authentication authentication,
                              Long cartItemId,
                              UpdateCartItemDto updateDto) {
        User authenticatedUser = userService.getAuthenticatedUser(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(authenticatedUser.getId());

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a cart item by id: "
                                                                + cartItemId));

        if (!ifShoppingCartContainsCartItem(shoppingCart, cartItemId)) {
            throw new IllegalUserAccessException("The user can't modify this cart item!");
        }

        cartItem.setQuantity(updateDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Authentication authentication, Long cartItemId) {
        User authenticatedUser = userService.getAuthenticatedUser(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUserId(authenticatedUser.getId());

        if (!ifShoppingCartContainsCartItem(shoppingCart, cartItemId)) {
            throw new IllegalUserAccessException("The user can't modify this cart item!");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public ShoppingCart create(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    private boolean isBookPresent(ShoppingCart shoppingCart, Long bookId) {
        return shoppingCart.getCartItems()
                .stream()
                .anyMatch(cartItem -> cartItem.getBook().getId().equals(bookId));
    }

    private boolean ifShoppingCartContainsCartItem(ShoppingCart shoppingCart, Long cartItemId) {
        return shoppingCart.getCartItems()
                .stream()
                .anyMatch(cartItem -> cartItem.getId().equals(cartItemId));
    }
}
