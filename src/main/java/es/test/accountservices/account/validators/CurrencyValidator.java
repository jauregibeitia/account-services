package es.test.accountservices.account.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;
import java.util.function.Predicate;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, Currency> {

    @Override
    public boolean isValid(Currency currency, ConstraintValidatorContext constraintValidatorContext) {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .anyMatch(Predicate.isEqual(currency));

    }
}

