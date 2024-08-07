package es.test.accountservices.account.dto;

import es.test.accountservices.account.validators.ValidCreateAccountRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.Currency;

@Value
@ValidCreateAccountRequestDto
public class CreateAccountRequestDto{
        @NotNull
        String accountName;

        @NotNull
        Currency currency;

        @NotNull
        Boolean treasury;

}
