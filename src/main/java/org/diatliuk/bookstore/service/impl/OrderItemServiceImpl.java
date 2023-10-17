package org.diatliuk.bookstore.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;
import org.diatliuk.bookstore.exception.EntityNotFoundException;
import org.diatliuk.bookstore.mapper.OrderItemMapper;
import org.diatliuk.bookstore.model.OrderItem;
import org.diatliuk.bookstore.repository.order.OrderItemRepository;
import org.diatliuk.bookstore.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemDto> getItemsByOrderId(Long id) {
        return orderItemRepository.getOrderItemsByOrder_Id(id)
                .stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto getItemInOrderById(Long orderId, Long id) {
        OrderItem item = orderItemRepository.getOrderItemsByOrder_Id(orderId)
                .stream().filter(orderItem -> orderItem.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Can't find an orderItem with id: "
                        + id + " in the order with id: " + orderId));
        return orderItemMapper.toDto(item);
    }
}
