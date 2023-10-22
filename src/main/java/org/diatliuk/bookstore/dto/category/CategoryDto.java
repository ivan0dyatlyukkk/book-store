package org.diatliuk.bookstore.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryDto {
    @NotBlank
    @Schema(example = "Fiction")
    private String name;
    @Schema(example = "Fiction books")
    private String description;
}
