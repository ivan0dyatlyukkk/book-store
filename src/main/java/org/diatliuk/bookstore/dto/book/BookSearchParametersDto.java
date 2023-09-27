package org.diatliuk.bookstore.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BookSearchParametersDto {
    @Schema(example = "The Great Gatsby")
    private String[] title;
    @Schema(example = "F. Scott Fitzgerald")
    private String[] author;
}
