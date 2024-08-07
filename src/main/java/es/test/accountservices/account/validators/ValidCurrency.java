package es.test.accountservices.account.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {CurrencyValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrency {

    String message() default "Currency that is not available";

    Class<?>[] groups() default {};

    Class[] payload() default {};
}