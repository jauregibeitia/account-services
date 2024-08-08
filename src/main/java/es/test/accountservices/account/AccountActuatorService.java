package es.test.accountservices.account;

import es.test.accountservices.account.client.ExchangeRateHttpClient;
import es.test.accountservices.account.dto.MoveFundsRequestDto;
import es.test.accountservices.account.exception.AccountNameAlreadyExistsException;
import es.test.accountservices.account.exception.AccountNotFoundException;
import es.test.accountservices.account.exception.NegativeBalanceForNonTreasuryAccountException;
import es.test.accountservices.account.model.Account;
import es.test.accountservices.account.model.CreateAccountRequest;
import es.test.accountservices.account.model.MoveFundsRequest;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Log4j2
@Service
public class AccountActuatorService {

    private final AccountRepository accountRepository;
    private final ExchangeRateHttpClient exchangeRateHttpClient;

    @Autowired
    public AccountActuatorService(
            AccountRepository accountRepository,
            ExchangeRateHttpClient exchangeRateHttpClient) {

        this.accountRepository = accountRepository;
        this.exchangeRateHttpClient = exchangeRateHttpClient;

    }

    public Account getAccountById(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        return account;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    Account createAccount(CreateAccountRequest createAccountRequest) {

        verifyAccountNameIsUniqueOrThrow(createAccountRequest);

        Account accountToCreate = createAccountRequestToNewAccount(createAccountRequest);

        val accountCreated = accountRepository.save(accountToCreate);

        return accountCreated;


    }

    private void verifyAccountNameIsUniqueOrThrow(CreateAccountRequest createAccountRequest) {
        if (accountRepository.findByAccountName(createAccountRequest.getAccountName()).isPresent()) {
            throw new AccountNameAlreadyExistsException(createAccountRequest.getAccountName());
        }
    }

    private Account createAccountRequestToNewAccount(CreateAccountRequest createAccountRequests) {
        return new Account(
                randomUUID(),
                createAccountRequests.getAccountName(),
                getCurrencyFromCurrencyCode(createAccountRequests.getCurrencyCode()),
                BigDecimal.ZERO,
                createAccountRequests.getTreasury()
        );
    }

    public Account moveFunds(MoveFundsRequest moveFundsRequest) throws JSONException {

        Account sourceAccount = accountRepository.findById(moveFundsRequest.getSourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException(moveFundsRequest.getSourceAccountId()));

        Account targetAccount = accountRepository.findById(moveFundsRequest.getTargetAccountId())
                .orElseThrow(() -> new AccountNotFoundException(moveFundsRequest.getTargetAccountId()));

        verifyNonTreasuryAccountBalanceOrThrow(moveFundsRequest, sourceAccount);

        var amount = convertAmountToDestinationAmountCurrency(
                sourceAccount.getCurrency().getCurrencyCode(),
                targetAccount.getCurrency().getCurrencyCode(),
                moveFundsRequest.getAmount());

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return sourceAccount;

    }

    private void verifyNonTreasuryAccountBalanceOrThrow(MoveFundsRequest moveFundsRequest, Account sourceAccount) {
        if (!sourceAccount.getTreasury()) {
            if (sourceAccount.getBalance().subtract(moveFundsRequest.getAmount()).compareTo(BigDecimal.ZERO) < 0 ) {
                throw new NegativeBalanceForNonTreasuryAccountException(sourceAccount.getAccountId());
            }
        }
    }

    private BigDecimal convertAmountToDestinationAmountCurrency(String fromCurrency, String toCurrency, BigDecimal amount) throws JSONException {
        if (!fromCurrency.equals(toCurrency)) {
            var rate = exchangeRateHttpClient.getRate(fromCurrency, toCurrency);
            return amount.multiply(rate);
        }
        else {
            return amount;
        }
    }

    private Currency getCurrencyFromCurrencyCode(String currencyCode){
        return Currency.getInstance(currencyCode);

    }
}

