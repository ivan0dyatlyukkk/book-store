package org.diatliuk.bookstore.service;

import java.util.List;
import org.diatliuk.bookstore.dto.order.CreateOrderRequestDto;
import org.diatliuk.bookstore.dto.order.OrderDto;
import org.diatliuk.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto save(CreateOrderRequestDto requestDto);

    List<OrderDto> getAll(Pageable pageable);

    OrderDto update(Long id, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemDto> getItemsByOrderId(Long id);

    OrderItemDto getItemInOrderById(Long orderId, Long id);
}
