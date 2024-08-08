package es.test.accountservices.account;

import es.test.accountservices.account.model.Account;
import lombok.Builder;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.UUID.randomUUID;

public class AccountDataFactory {

    @Builder(builderMethodName = "acountWithDefaults", builderClassName = "AccountBuilder")
    public static Account newAccount() {
        return new Account(
                randomUUID(),
                RandomStringUtils.random(10),
                getRandomCurrencyCode(),
                new BigDecimal(BigInteger.valueOf(new Random().nextInt(100001)), 2),
                getRandomBoolean()
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
