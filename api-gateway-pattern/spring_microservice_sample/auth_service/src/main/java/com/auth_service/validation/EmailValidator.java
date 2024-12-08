package com.auth_service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN = "^\\w*?[a-zA-Z]\\w+@[a-z\\d\\-]+(\\.[a-z\\d\\-]+)*\\.[a-z]+\\z";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && Pattern.matches(EMAIL_PATTERN, email);
    }
}
