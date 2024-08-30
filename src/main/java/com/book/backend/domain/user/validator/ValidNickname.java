package com.book.backend.domain.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NicknameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidNickname {
    String message() default "Invalid nickname";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}