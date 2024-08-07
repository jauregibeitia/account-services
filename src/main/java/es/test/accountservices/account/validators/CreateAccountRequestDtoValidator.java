package es.test.accountservices.account.validators;

import es.test.accountservices.account.dto.CreateAccountRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;
import java.util.function.Predicate;

public class  CreateAccountRequestDtoValidator implements ConstraintValidator<ValidCreateAccountRequestDto, CreateAccountRequestDto> {

    @Override
    public boolean isValid(CreateAccountRequestDto createAccountRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .anyMatch(Predicate.isEqual(createAccountRequestDto.getCurrency()));

    }
}

