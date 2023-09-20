package org.diatliuk.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.dto.cart.item.UpdateCartItemDto;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserService userService;
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
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find a cart item by id: "
                                                                + cartItemId));
        cartItem.setQuantity(updateDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    private boolean isBookPresent(ShoppingCart shoppingCart, Long bookId) {
        return shoppingCart.getCartItems()
                .stream()
                .anyMatch(cartItem -> cartItem.getBook().getId().equals(bookId));
    }
}
