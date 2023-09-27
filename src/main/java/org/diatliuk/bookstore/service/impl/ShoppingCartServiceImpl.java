package org.diatliuk.bookstore.service.impl;

import java.util.HashSet;
import java.util.List;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Lazy
    @Autowired
    private UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto get() {
        User authenticatedUser = userService.getAuthenticatedUser();
        ShoppingCart shoppingCart = shoppingCartRepository
                                    .getShoppingCartByUserId(authenticatedUser.getId());
        shoppingCart.setCartItems(new HashSet<>(cartItemRepository
                .findAllByShoppingCartId(shoppingCart.getId()))
        );
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find a book by id + "
                        + requestDto.getBookId()));
        cartItem.setBook(book);

        User authenticatedUser = userService.getAuthenticatedUser();
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

    @Override
    public CartItemDto update(Long cartItemId, UpdateCartItemDto updateDto) {
        User authenticatedUser = userService.getAuthenticatedUser();

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a cart item by id: "
                                                                + cartItemId));
        if (!isUserAbleToModifyItem(authenticatedUser.getId(), cartItemId)) {
            throw new IllegalUserAccessException("The user can't modify this cart item!");
        }

        cartItem.setQuantity(updateDto.getQuantity());
        CartItem savedItem = cartItemRepository.save(cartItem);
        savedItem.setBook(cartItem.getBook());
        return cartItemMapper.toDto(savedItem);
    }

    @Override
    public void deleteById(Long cartItemId) {
        User authenticatedUser = userService.getAuthenticatedUser();

        if (!isUserAbleToModifyItem(authenticatedUser.getId(), cartItemId)) {
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

    private boolean isUserAbleToModifyItem(Long userId, Long cartItemId) {
        Long cartId = shoppingCartRepository.getShoppingCartByUserId(userId).getId();
        List<CartItem> cartItems = cartItemRepository.findAllByShoppingCartId(cartId);
        return cartItems.stream().anyMatch(cartItem -> cartItem.getId().equals(cartItemId));
    }
}
