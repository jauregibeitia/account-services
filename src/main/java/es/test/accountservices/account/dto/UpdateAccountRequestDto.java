package es.test.accountservices.account.dto;

import es.test.accountservices.account.validators.ValidCurrency;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateAccountRequestDto(
        @NotNull
        UUID accountId,

        String accountName,
        @ValidCurrency
        String currencyCode) {
}
