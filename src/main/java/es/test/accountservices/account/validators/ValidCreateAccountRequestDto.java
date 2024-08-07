package es.test.accountservices.account.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {CreateAccountRequestDtoValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCreateAccountRequestDto {

    String message() default "Currency that is not available";

    Class<?>[] groups() default {};

    Class[] payload() default {};
}