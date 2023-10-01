package org.diatliuk.bookstore.service;

import java.util.List;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;

public interface OrderItemService {
    List<OrderItemDto> getItemsByOrderId(Long id);

    OrderItemDto getItemInOrderById(Long orderId, Long id);
}
