package es.test.accountservices.account.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException{

    private static final String MESSAGE = "Account with accountId=%s not found";

    public AccountNotFoundException(UUID accountId) {
        super(String.format(MESSAGE, accountId));
    }
}
