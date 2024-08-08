package es.test.accountservices.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException{

    private static final String MESSAGE = "Account with accountId=%s not found";

    public AccountNotFoundException(UUID accountId) {
        super(String.format(MESSAGE, accountId));
    }
}
