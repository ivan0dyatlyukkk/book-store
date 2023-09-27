package org.diatliuk.bookstore.mapper;

import org.diatliuk.bookstore.config.MapperConfig;
import org.diatliuk.bookstore.dto.cart.ShoppingCartDto;
import org.diatliuk.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "shoppingCart.user.id")
    @Mapping(target = "cartItems", source = "cartItems", qualifiedByName = "cartItemToDto")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
