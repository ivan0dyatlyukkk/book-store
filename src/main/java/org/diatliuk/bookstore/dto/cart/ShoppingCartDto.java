package org.diatliuk.bookstore.dto.cart;

import java.util.Set;
import lombok.Data;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
