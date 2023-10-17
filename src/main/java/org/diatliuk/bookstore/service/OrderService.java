package org.diatliuk.bookstore.service;

import java.util.List;
import org.diatliuk.bookstore.dto.order.CreateOrderRequestDto;
import org.diatliuk.bookstore.dto.order.OrderDto;
import org.diatliuk.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderDto save(Authentication authentication, CreateOrderRequestDto requestDto);

    List<OrderDto> getAll(Pageable pageable);

    OrderDto update(Long id, UpdateOrderStatusRequestDto requestDto);
}
