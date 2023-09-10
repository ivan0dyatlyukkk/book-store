package org.diatliuk.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotNull
    @NotBlank
    @Schema(example = "The Clean Code")
    private String title;

    @NotNull
    @NotBlank
    @Schema(example = "Robert C. Martin")
    private String author;

    @NotNull
    @ISBN
    @Schema(example = "978-161-729-045-9")
    private String isbn;

    @NotNull
    @Min(0)
    @Schema(example = "210.0")
    private BigDecimal price;

    @Schema(example = "The book description")
    private String description;

    @Schema(example = "https://example.com/updated-cover-image.jpg")
    private String coverImage;
}
