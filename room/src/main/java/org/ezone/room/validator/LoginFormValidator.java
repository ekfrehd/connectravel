package org.ezone.room.validator;
import org.ezone.room.dto.MemberFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LoginFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberFormDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberFormDto memberFormDto = (MemberFormDto) target;

        // email 필드 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required", "Please enter your email.");

        // password 필드 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required", "Please enter your password.");
    }
}