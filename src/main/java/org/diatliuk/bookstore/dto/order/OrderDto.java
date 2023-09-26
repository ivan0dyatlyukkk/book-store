package org.diatliuk.bookstore.dto.order;

import lombok.Data;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItems;
    private String orderDate;
    private BigDecimal total;
    private String status;
}
