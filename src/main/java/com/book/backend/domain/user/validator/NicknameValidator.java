package com.book.backend.domain.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<ValidNickname, String> {

    private static final Pattern KOREAN_PATTERN = Pattern.compile("^[가-힣]{2,8}$");
    private static final Pattern ENGLISH_PATTERN = Pattern.compile("^[a-zA-Z]{3,16}$");

    @Override
    public void initialize(ValidNickname constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return KOREAN_PATTERN.matcher(value).matches() || ENGLISH_PATTERN.matcher(value).matches();
    }
}