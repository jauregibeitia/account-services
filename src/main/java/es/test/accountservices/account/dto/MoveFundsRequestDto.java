package es.test.accountservices.account.dto;

import es.test.accountservices.account.validators.ValidNotSameAccountId;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@ValidNotSameAccountId
public class MoveFundsRequestDto {
        @NotNull
        UUID sourceAccountId;

        @NotNull
        UUID targetAccountId;

        @NotNull
        BigDecimal amount;
}
