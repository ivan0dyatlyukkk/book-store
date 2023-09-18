package org.diatliuk.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.book.AddBookToCartRequestDto;
import org.diatliuk.bookstore.dto.book.BookQuantityDto;
import org.diatliuk.bookstore.model.ShoppingCart;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    @GetMapping
    public ShoppingCart getById() {
        return null;
    }

    @PostMapping
    public ShoppingCart addItem(@RequestBody @Valid AddBookToCartRequestDto book) {
        return null;
    }

    @PutMapping("cart-items/{cartItemId}")
    public ShoppingCart updateItemQuantity(@PathVariable Long cartItemId,
                                           @RequestBody @Valid BookQuantityDto quantity) {
        return null;
    }

    @DeleteMapping("cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemById(@PathVariable Long cartItemId) {

    }
}
