package org.diatliuk.bookstore.repository.cart;

import java.util.List;
import java.util.Optional;
import org.diatliuk.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("FROM CartItem ci LEFT JOIN FETCH ci.book "
            + "LEFT JOIN FETCH ci.shoppingCart WHERE ci.id = :id")
    Optional<CartItem> findById(Long id);

    @Query("FROM CartItem ci LEFT JOIN FETCH ci.book "
            + "LEFT JOIN FETCH ci.shoppingCart WHERE ci.shoppingCart.id = :id")
    List<CartItem> findAllByShoppingCartId(Long id);
}
