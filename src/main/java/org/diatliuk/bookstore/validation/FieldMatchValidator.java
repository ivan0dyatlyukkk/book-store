package org.diatliuk.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.diatliuk.bookstore.annotation.FieldMatch;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        Object fieldValue = new BeanWrapperImpl(obj).getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(obj).getPropertyValue(fieldMatch);

        return (fieldMatch == fieldMatchValue)
                || (fieldValue != null
                && fieldValue.equals(fieldMatchValue));
    }
}
