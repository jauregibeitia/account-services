package es.test.accountservices.account.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Currency;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Document(collection = "account")
@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Account {
    @Id UUID accountId;

    @NotEmpty
    String accountName;

    @NotEmpty
    Currency currency;

    @NotEmpty
    BigDecimal balance;

    @NotEmpty
    Boolean treasury;

}

