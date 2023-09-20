package org.diatliuk.bookstore.repository.cart;

import org.diatliuk.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
