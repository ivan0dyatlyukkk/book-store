package org.diatliuk.bookstore.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginRequestDto {
    @NotBlank
    @Schema(example = "new@example.com")
    private String email;

    @NotBlank
    @Length(min = 8)
    private String password;
}
