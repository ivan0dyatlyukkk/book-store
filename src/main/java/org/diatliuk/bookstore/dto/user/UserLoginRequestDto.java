package org.diatliuk.bookstore.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8)
    private String password;
}
