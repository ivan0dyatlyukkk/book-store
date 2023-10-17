package org.diatliuk.bookstore.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotBlank
    @Schema(example = "DELIVERED")
    private String status;
}
