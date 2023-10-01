package org.diatliuk.bookstore.service;

import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.dto.cart.item.UpdateCartItemDto;
import org.diatliuk.bookstore.model.ShoppingCart;
import org.diatliuk.bookstore.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto get(Authentication authentication);

    CartItemDto save(Authentication authentication, CreateCartItemRequestDto requestDto);

    CartItemDto update(Authentication authentication, Long cartItemId, UpdateCartItemDto quantity);

    void deleteById(Authentication authentication, Long cartItemId);

    ShoppingCart create(User user);
}
