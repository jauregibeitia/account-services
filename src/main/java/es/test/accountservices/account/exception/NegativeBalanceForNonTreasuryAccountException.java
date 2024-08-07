package es.test.accountservices.account.exception;

import java.util.UUID;

public class NegativeBalanceForNonTreasuryAccountException extends RuntimeException{

    private static final String MESSAGE = "Not enough balance in account with accountId=%s";

    public NegativeBalanceForNonTreasuryAccountException(UUID accountId) {
        super(String.format(MESSAGE, accountId));
    }
}
