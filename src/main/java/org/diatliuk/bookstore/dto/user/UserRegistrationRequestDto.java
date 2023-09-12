package org.diatliuk.bookstore.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.diatliuk.bookstore.annotation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch.List({
        @FieldMatch(
                field = "password",
                fieldMatch = "repeatPassword",
                message = "The password and repeat password aren't the same"
        )
})
public class UserRegistrationRequestDto {
    @Email
    @Schema(example = "new@example.com")
    private String email;

    @NotBlank
    @Length(min = 8)
    private String password;

    @NotBlank
    @Length(min = 8)
    private String repeatPassword;

    @NotBlank
    @Schema(example = "John")
    private String firstName;

    @NotBlank
    @Schema(example = "Brown")
    private String lastName;

    @Schema(example = "123 Main St, City, Country")
    private String shippingAddress;
}
