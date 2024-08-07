package es.test.accountservices.account.exception;

public class AccountNameAlreadyExistsException extends RuntimeException{

    private static final String MESSAGE = "Account with name=%s already exists";

    public AccountNameAlreadyExistsException(String accountName) {
        super(String.format(MESSAGE, accountName));
    }
}
