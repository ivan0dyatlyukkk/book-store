package org.diatliuk.bookstore.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotBlank
    @Schema(example = "Kyiv, Shevchenko ave, 1")
    private String shippingAddress;
}
