package es.test.accountservices.account;

import es.test.accountservices.account.dto.CreateAccountRequestDto;
import es.test.accountservices.account.dto.MoveFundsRequestDto;
import es.test.accountservices.account.model.Account;
import es.test.accountservices.account.model.CreateAccountRequest;
import es.test.accountservices.account.model.MoveFundsRequest;
import lombok.Builder;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

public class AccountDataFactory {

    @Builder(builderMethodName = "acountWithDefaults", builderClassName = "AccountBuilder")
    public static Account newAccount(
            UUID accountId,
            String accountName,
            Currency currency,
            BigDecimal balance,
            Boolean treasury
    ) {
        return new Account(
                ofNullable(accountId).orElse(randomUUID()),
                ofNullable(accountName).orElse(RandomStringUtils.random(10)),
                ofNullable(currency).orElse(getRandomCurrencyCode()),
                ofNullable(balance).orElse(new BigDecimal(BigInteger.valueOf(new Random().nextInt(100001)), 2)),
                ofNullable(treasury).orElse(getRandomBoolean())
        );
    }

    @Builder(builderMethodName = "createAccountRequestDtoWithDefaults", builderClassName = "CreateAccountRequestDtoBuilder")
    public static CreateAccountRequestDto newCreateAccountRequestDto(
            String accountName,
            String currencyCode,
            Boolean treasury
    ) {
        return new CreateAccountRequestDto(
                ofNullable(accountName).orElse(RandomStringUtils.random(10)),
                ofNullable(currencyCode).orElse(getRandomCurrencyCode().getCurrencyCode()),
                ofNullable(treasury).orElse(getRandomBoolean())
        );
    }

    @Builder(builderMethodName = "createMoveFundsRequestDtoWithDefaults", builderClassName = "MoveFundsRequestDtoBuilder")
    public static MoveFundsRequestDto newMoveFundsRequestDto(
            UUID sourceAccountId,
            UUID targetAccountId,
            BigDecimal amount
    ) {
        return new MoveFundsRequestDto(
                ofNullable(sourceAccountId).orElse(randomUUID()),
                ofNullable(targetAccountId).orElse(randomUUID()),
                ofNullable(amount).orElse(new BigDecimal(BigInteger.valueOf(new Random().nextInt(100001)), 2))
        );
    }

    @Builder(builderMethodName = "createCreateAccountRequestWithDefaults", builderClassName = "CreateAccountRequestBuilder")
    public static CreateAccountRequest newCreateAccountRequest(
            String accountName,
            String currencyCode,
            Boolean treasury
    ) {
        return new CreateAccountRequest(
                ofNullable(accountName).orElse(RandomStringUtils.random(10)),
                ofNullable(currencyCode).orElse(getRandomCurrencyCode().getCurrencyCode()),
                ofNullable(treasury).orElse(getRandomBoolean())
        );
    }

    @Builder(builderMethodName = "createMoveFundsRequestWithDefaults", builderClassName = "MoveFundsRequestBuilder")
    public static MoveFundsRequest newMoveFundsRequest(
            UUID sourceAccountId,
            UUID targetAccountId,
            BigDecimal amount
    ) {
        return new MoveFundsRequest(
                ofNullable(sourceAccountId).orElse(randomUUID()),
                ofNullable(targetAccountId).orElse(randomUUID()),
                ofNullable(amount).orElse(new BigDecimal(BigInteger.valueOf(new Random().nextInt(100001)), 2))
        );
    }

    public static Currency getRandomCurrencyCode(){
        var currencies = Currency.getAvailableCurrencies();
        var arrayCurrencies = currencies.toArray(new Currency[currencies.size()]);
        int randomIndex = ThreadLocalRandom.current().nextInt(currencies.size());
        var randomCurrency = arrayCurrencies[randomIndex];
        return randomCurrency;

    }

    public static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public static UUID randomAccountId(){
        return randomUUID();
    }


}
