package es.test.accountservices.account.validators;

import es.test.accountservices.account.dto.MoveFundsRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotSameAccountIdValidator implements ConstraintValidator<ValidNotSameAccountId, MoveFundsRequestDto> {

    @Override
    public boolean isValid(MoveFundsRequestDto moveFundsRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        return !moveFundsRequestDto.getSourceAccountId().equals(moveFundsRequestDto.getTargetAccountId());

    }
}

