package es.test.accountservices.account.dto;

import es.test.accountservices.account.validators.ValidCurrency;
import es.test.accountservices.account.validators.ValidNotSameAccountId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateAccountRequestDto(
        @NotNull
        UUID accountId,

        String accountName,
        @ValidCurrency
        String currencyCode) {
}
