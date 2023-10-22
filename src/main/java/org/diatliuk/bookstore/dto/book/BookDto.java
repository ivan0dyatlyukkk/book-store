package org.diatliuk.bookstore.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Long id;
    @Schema(example = "Moby-Dick")
    private String title;
    @Schema(example = "Joseph Heller")
    private String author;
    @Schema(example = "978-161-729-045-9")
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}
