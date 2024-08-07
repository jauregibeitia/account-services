package es.test.accountservices.account.dto;

import es.test.accountservices.account.validators.ValidCurrency;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.Currency;

@Value
public class CreateAccountRequestDto{
        @NotNull
        String accountName;

        @NotNull
        @ValidCurrency
        Currency currency;

        @NotNull
        Boolean treasury;

}
