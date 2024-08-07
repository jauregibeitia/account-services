package es.test.accountservices.account.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class MoveFundsRequest {

    UUID sourceAccountId;

    UUID targetAccountId;

    BigDecimal amount;
}
