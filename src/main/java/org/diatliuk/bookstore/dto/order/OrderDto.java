package org.diatliuk.bookstore.dto.order;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.diatliuk.bookstore.dto.order.item.OrderItemDto;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItems;
    private String orderDate;
    private BigDecimal total;
    private String status;
}
