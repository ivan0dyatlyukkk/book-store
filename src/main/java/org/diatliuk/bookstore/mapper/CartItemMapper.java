package org.diatliuk.bookstore.mapper;

import org.diatliuk.bookstore.config.MapperConfig;
import org.diatliuk.bookstore.dto.cart.item.CartItemDto;
import org.diatliuk.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Named("cartItemToDto")
    @Mapping(target = "bookId", source = "cartItem.book.id")
    @Mapping(target = "bookTitle", source = "cartItem.book.title")
    CartItemDto toDto(CartItem cartItem);
}
