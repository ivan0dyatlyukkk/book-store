package org.diatliuk.bookstore.dto.order;

import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    private String status;
}