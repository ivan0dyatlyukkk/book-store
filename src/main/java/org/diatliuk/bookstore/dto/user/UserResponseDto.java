package org.diatliuk.bookstore.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;

    @Schema(example = "new@example.com")
    private String email;

    private String firstName;

    private String lastName;

    @Schema(example = "123 Main St, City, Country")
    private String shippingAddress;
}
