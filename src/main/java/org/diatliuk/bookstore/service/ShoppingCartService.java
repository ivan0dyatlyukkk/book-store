package org.diatliuk.bookstore.service;

import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.dto.cart.item.UpdateCartItemDto;
import org.diatliuk.bookstore.model.ShoppingCart;
import org.diatliuk.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto get();

    CartItemDto save(CreateCartItemRequestDto requestDto);

    CartItemDto update(Long cartItemId, UpdateCartItemDto quantity);

    void deleteById(Long cartItemId);

    ShoppingCart create(User user);
}
