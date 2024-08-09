package es.test.accountservices.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UpdateAccountRequest {

    UUID accountId;

    String accountName;

    String currencyCode;

}
