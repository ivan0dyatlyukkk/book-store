package org.diatliuk.bookstore.service;

import java.util.List;
import org.diatliuk.bookstore.dto.order.CreateOrderRequestDto;
import org.diatliuk.bookstore.dto.order.OrderDto;
import org.diatliuk.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;

public interface OrderService {
    OrderDto save();

    OrderDto save(CreateOrderRequestDto requestDto);

    List<OrderDto> getAll();

    OrderDto getById(Long id);

    OrderDto update(Long id, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemDto> getItemsByOrderId(Long id);

    OrderItemDto getItemInOrderById(Long orderId, Long id);
}
