package es.test.accountservices.account.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Value
public class AccountDto {

    UUID accountId;

    String accountName;

    Currency currency;

    BigDecimal balance;

    Boolean treasury;

}