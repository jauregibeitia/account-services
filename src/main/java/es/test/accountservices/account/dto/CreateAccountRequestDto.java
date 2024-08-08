package es.test.accountservices.account.dto;

import es.test.accountservices.account.validators.ValidCurrency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequestDto(
        @NotNull
        @NotBlank
        String accountName,
        @NotNull
        @ValidCurrency
        String currencyCode,
        @NotNull
        Boolean treasury) {
}
