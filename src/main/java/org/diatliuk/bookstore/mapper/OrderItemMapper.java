package org.diatliuk.bookstore.mapper;

import org.diatliuk.bookstore.config.MapperConfig;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;
import org.diatliuk.bookstore.model.CartItem;
import org.diatliuk.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Named("orderItemToDto")
    @Mapping(target = "bookId", source = "orderItem.book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "price", source = "cartItem.book.price")
    OrderItem cartItemToOrderItem(CartItem cartItem);
}
