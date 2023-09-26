package org.diatliuk.bookstore.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.diatliuk.bookstore.dto.order.CreateOrderRequestDto;
import org.diatliuk.bookstore.dto.order.OrderDto;
import org.diatliuk.bookstore.dto.order.UpdateOrderStatusRequestDto;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;
import org.diatliuk.bookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<OrderDto> getAll(Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderDto save(@RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.save(requestDto);
    }

    @GetMapping("/{id}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<OrderItemDto> getOrderItemsByOrderId(@PathVariable Long id) {
        return orderService.getItemsByOrderId(id);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderItemDto getItemInOrderById(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getItemInOrderById(orderId, itemId);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDto update(@PathVariable Long id,
                           @RequestBody @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.update(id, requestDto);
    }
}
