package es.test.accountservices.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Currency;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateAccountRequest {

    String accountName;

    String currencyCode;

    Boolean treasury;
}
