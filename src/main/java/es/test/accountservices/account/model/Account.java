package es.test.accountservices.account.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Currency;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    UUID accountId;

    @NotEmpty
    String accountName;

    @NotNull
    Currency currency;

    @NotNull
    BigDecimal balance;

    @NotNull
    Boolean treasury;

}

