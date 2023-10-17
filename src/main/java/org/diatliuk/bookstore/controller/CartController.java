package org.diatliuk.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.diatliuk.bookstore.dto.cart.item.UpdateCartItemDto;
import org.diatliuk.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoint for managing shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get a shopping cart", description = "Allow to get all info "
                                            + "about a user's shopping cart")
    public ShoppingCartDto get() {
        return shoppingCartService.get();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Save a new cart item to the shopping cart", description = "Allow to save"
            + " a new cart item to the shopping cart")
    public CartItemDto saveItem(@RequestBody @Valid CreateCartItemRequestDto createDto) {
        return shoppingCartService.save(createDto);
    }

    @PutMapping("cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update a cart item", description = "Allow to update a quantity of "
            + "the cart item by cart item id")
    public CartItemDto updateItemQuantity(@PathVariable Long cartItemId,
                                           @RequestBody @Valid UpdateCartItemDto updateDto) {
        return shoppingCartService.update(cartItemId, updateDto);
    }

    @DeleteMapping("cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete a cart item", description = "Allow to delete "
                                                + "a particular cart item by its id")
    public void deleteItemById(@PathVariable Long cartItemId) {
        shoppingCartService.deleteById(cartItemId);
    }
}
