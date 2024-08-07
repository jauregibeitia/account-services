package es.test.accountservices.account.dto;

import es.test.accountservices.account.validators.ValidCurrency;
import es.test.accountservices.account.validators.ValidNotSameAccountId;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Value
public class MoveFundsRequestDto {
        @NotNull
        UUID sourceAccountId;

        @NotNull
        @ValidNotSameAccountId
        UUID targetAccountId;

        @NotNull
        @ValidCurrency
        Currency currency;

        @NotNull
        BigDecimal amount;

}
