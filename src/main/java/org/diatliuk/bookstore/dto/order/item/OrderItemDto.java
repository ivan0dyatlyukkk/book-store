package org.diatliuk.bookstore.dto.order.item;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private Integer quantity;
}
