package es.test.accountservices.account.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {NotSameAccountIdValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNotSameAccountId {

    String message() default "Source and target accounts are the same";

    Class<?>[] groups() default {};

    Class[] payload() default {};
}