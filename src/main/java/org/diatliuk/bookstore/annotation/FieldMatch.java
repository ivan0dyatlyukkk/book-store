package org.diatliuk.bookstore.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.diatliuk.bookstore.validation.RepeatPasswordValidator;

@Constraint(validatedBy = RepeatPasswordValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMatch {
    String message() default "The password and the repeat password aren't the same";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
