package org.diatliuk.bookstore.dto.cart.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class CreateCartItemRequestDto {
    @NotNull
    private Long bookId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
