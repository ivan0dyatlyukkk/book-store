package org.diatliuk.bookstore.service;

import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.dto.cart.item.UpdateCartItemDto;

public interface ShoppingCartService {
    ShoppingCartDto get();

    CartItemDto save(CreateCartItemRequestDto requestDto);

    CartItemDto update(Long cartItemId, UpdateCartItemDto quantity);

    void deleteById(Long cartItemId);
}
