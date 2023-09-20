package org.diatliuk.bookstore.repository.cart;

import org.diatliuk.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart sc"
            + " LEFT JOIN FETCH sc.user"
            + " LEFT JOIN FETCH sc.cartItems"
            + " WHERE sc.user.id = ?1")
    ShoppingCart getShoppingCartByUserId(Long id);
}
