package es.test.accountservices.account;

import es.test.accountservices.account.client.ExchangeRateHttpClient;
import es.test.accountservices.account.exception.AccountNameAlreadyExistsException;
import es.test.accountservices.account.exception.AccountNotFoundException;
import es.test.accountservices.account.exception.NegativeBalanceForNonTreasuryAccountException;
import es.test.accountservices.account.model.Account;
import es.test.accountservices.account.model.MoveFundsRequest;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class AccountActuatorServiceTest {

    @BeforeEach
    void setup() {
        accountActuatorService =
                new AccountActuatorService(accountRepository,
                        exchangeRateHttpClient);
    }

    @MockBean
    private ExchangeRateHttpClient exchangeRateHttpClient;

    @MockBean
    private AccountRepository accountRepository;

    private AccountActuatorService accountActuatorService;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    void findAccountByAccountId_success() {
        //Given
        var account = AccountDataFactory.acountWithDefaults().build();
        given(accountRepository.findById(account.getAccountId())).willReturn(Optional.of(account));
        //When
        var result = accountActuatorService.getAccountById(account.getAccountId());
        //Then
        assertThat(result).isEqualTo(account);
    }

    @Test
    void givenNonExistingAccount_findAccountByAccountId_thenExceptionIsThrown() {
        //Given
        given(accountRepository.findById(any())).willReturn(Optional.empty());
        //Then
        assertThrows(AccountNotFoundException.class, () -> accountActuatorService.getAccountById(any()),
                "Account with accountId=" + AccountDataFactory.randomAccountId() + " not found");
    }

    @Test
    void findAllAccounts_success() {
        //Given
        var account_one = AccountDataFactory.acountWithDefaults().build();
        var account_two = AccountDataFactory.acountWithDefaults().build();
        var accounts = new ArrayList<Account>();
        Collections.addAll(accounts, account_one, account_two);
        given(accountRepository.findAll()).willReturn(accounts);
        //When
        var result = accountActuatorService.getAllAccounts();
        //Then
        assertThat(result).containsExactlyInAnyOrder(account_one, account_two);

    }

    @Test
    void givenNoAccounts_findAllAccounts_thenReturnEmptyList() {
        //Given
        given(accountRepository.findAll()).willReturn(List.of());
        //When
        var result = accountActuatorService.getAllAccounts();
        //Then
        assertThat(result).isEmpty();
    }

    @Test
    void createAccount_success() {
        //Given
        var account = AccountDataFactory.acountWithDefaults().build();
        var createAccountRequest = AccountDataFactory.createCreateAccountRequestWithDefaults()
                .accountName(account.getAccountName())
                .currencyCode(account.getCurrency().getCurrencyCode())
                .treasury(account.getTreasury())
                .build();
        //When
        accountActuatorService.createAccount(createAccountRequest);
        //Then
        verifyAccountWasSavedCorrectly(account);
    }

    @Test
    void createAccount_accountNameAlreadyExists_ThenExceptionIsThrown() {
        //Given
        var account = AccountDataFactory.acountWithDefaults().build();
        var createAccountRequest = AccountDataFactory.createCreateAccountRequestWithDefaults()
                .accountName(account.getAccountName())
                .build();
        given(accountRepository.findByAccountName(createAccountRequest.getAccountName())).willReturn(Optional.of(account));
        //Then
        assertThrows(AccountNameAlreadyExistsException.class, () -> accountActuatorService.createAccount(createAccountRequest),
                "Account with name=" + account.getAccountName() + " already exists");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void moveFunds_success() throws JSONException {
        //Given
        var sourceAccount = givenAccountWithBalance(BigDecimal.valueOf(10));
        var targetAccount = givenAccountWithBalance(BigDecimal.valueOf(4));
        var moveFundsRequest = givenNewMoveFundsRequest(sourceAccount, targetAccount, BigDecimal.valueOf(8));

        given(accountRepository.findById(sourceAccount.getAccountId())).willReturn(Optional.of(sourceAccount));
        given(accountRepository.findById(targetAccount.getAccountId())).willReturn(Optional.of(targetAccount));
        given(exchangeRateHttpClient.getRate(
                sourceAccount.getCurrency().getCurrencyCode(),
                targetAccount.getCurrency().getCurrencyCode()
        )).willReturn(BigDecimal.valueOf(1));
        //When
        accountActuatorService.moveFunds(moveFundsRequest);
        //Then
        verify(accountRepository, times(2)).save(any());
        assertThat(sourceAccount.getBalance()).isEqualTo(BigDecimal.valueOf(2));
        assertThat(targetAccount.getBalance()).isEqualTo(BigDecimal.valueOf(12));
    }

    @Test
    void moveFunds_negativeBalanceInTreasuryAccount_success() throws JSONException {
        //Given
        var sourceAccount = givenAccountWithBalance(BigDecimal.valueOf(2));
        sourceAccount.setTreasury(true);
        var targetAccount = givenAccountWithBalance(BigDecimal.valueOf(4));
        var moveFundsRequest = givenNewMoveFundsRequest(sourceAccount, targetAccount, BigDecimal.valueOf(8));

        given(accountRepository.findById(sourceAccount.getAccountId())).willReturn(Optional.of(sourceAccount));
        given(accountRepository.findById(targetAccount.getAccountId())).willReturn(Optional.of(targetAccount));
        given(exchangeRateHttpClient.getRate(
                sourceAccount.getCurrency().getCurrencyCode(),
                targetAccount.getCurrency().getCurrencyCode()
        )).willReturn(BigDecimal.valueOf(1));
        //When
        accountActuatorService.moveFunds(moveFundsRequest);
        //Then
        verify(accountRepository, times(2)).save(any());
        assertThat(sourceAccount.getBalance()).isEqualTo(BigDecimal.valueOf(-6));
        assertThat(targetAccount.getBalance()).isEqualTo(BigDecimal.valueOf(12));
    }

    @Test
    void moveFunds_negativeBalanceInNonTreasuryAccount_ThenExceptionIsThrown() throws JSONException {
        //Given
        var sourceAccount = givenAccountWithBalance(BigDecimal.valueOf(2));
        sourceAccount.setTreasury(false);
        var targetAccount = givenAccountWithBalance(BigDecimal.valueOf(4));
        var moveFundsRequest = givenNewMoveFundsRequest(sourceAccount, targetAccount, BigDecimal.valueOf(8));
        //EWhen
        given(accountRepository.findById(sourceAccount.getAccountId())).willReturn(Optional.of(sourceAccount));
        given(accountRepository.findById(targetAccount.getAccountId())).willReturn(Optional.of(targetAccount));
        given(exchangeRateHttpClient.getRate(
                sourceAccount.getCurrency().getCurrencyCode(),
                targetAccount.getCurrency().getCurrencyCode()
        )).willReturn(BigDecimal.valueOf(1));
        //Then
        verify(accountRepository, never()).save(any());
        assertThrows(NegativeBalanceForNonTreasuryAccountException.class, () -> accountActuatorService.moveFunds(moveFundsRequest),
                "Not enough balance in account with accountId=%s" + sourceAccount.getAccountName());
    }

    @Test
    void moveFundsToNonExistingAccount_ThenExceptionIsThrown() {
        //Given
        var moveFundsRequest = AccountDataFactory.createMoveFundsRequestWithDefaults().build();
        //Then
        assertThrows(AccountNotFoundException.class, () -> accountActuatorService.moveFunds(moveFundsRequest),
                "Account with accountId=" + moveFundsRequest.getSourceAccountId() + " not found");
    }

    @Test
    void updateAccount_success_accountIsUpdated() throws JSONException {
        //Given
        var account = givenAccountWithBalance(BigDecimal.valueOf(2));
        var updateAccountRequest = AccountDataFactory.createUpdateAccountRequestWithDefaults()
                .accountId(account.getAccountId())
                .build();
        given(accountRepository.findById(updateAccountRequest.getAccountId())).willReturn(Optional.of(account));
        given(exchangeRateHttpClient.getRate(
                account.getCurrency().getCurrencyCode(),
                updateAccountRequest.getCurrencyCode()
        )).willReturn(BigDecimal.valueOf(2));
        //When
        accountActuatorService.updateAccount(updateAccountRequest);
        //Then
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void updateAccount_success_accountIsNotUpdated() throws JSONException {
        //Given
        var account = givenAccountWithBalance(BigDecimal.valueOf(2));
        var updateAccountRequest = AccountDataFactory.createUpdateAccountRequestWithDefaults()
                .accountId(account.getAccountId())
                .accountName(account.getAccountName())
                .currencyCode(account.getCurrency().getCurrencyCode())
                .build();
        given(accountRepository.findById(updateAccountRequest.getAccountId())).willReturn(Optional.of(account));
        given(exchangeRateHttpClient.getRate(
                account.getCurrency().getCurrencyCode(),
                updateAccountRequest.getCurrencyCode()
        )).willReturn(BigDecimal.valueOf(2));
        //When
        accountActuatorService.updateAccount(updateAccountRequest);
        //Then
        verify(accountRepository, never()).save(any());
    }

    @Test
    void updateAccount_accountIdNotExisting_ThenExceptionIsThrown() {
        //Given
        var updateAccountRequest = AccountDataFactory.createUpdateAccountRequestWithDefaults().build();
        //Then
        assertThrows(AccountNotFoundException.class, () -> accountActuatorService.updateAccount(updateAccountRequest),
                "Account with accountId=" + updateAccountRequest.getAccountId() + " not found");
    }

    @Test
    void updateAccount_accountNameAlreadyExists_ThenExceptionIsThrown() throws JSONException {
        var account = AccountDataFactory.acountWithDefaults().build();
        var updateAccountRequest = AccountDataFactory.createUpdateAccountRequestWithDefaults()
                .accountName(account.getAccountName())
                .build();
        given(accountRepository.findById(updateAccountRequest.getAccountId())).willReturn(Optional.of(account));
        given(accountRepository.findByAccountName(updateAccountRequest.getAccountName())).willReturn(Optional.of(account));
        //Then
        assertThrows(AccountNameAlreadyExistsException.class, () -> accountActuatorService.updateAccount(updateAccountRequest),
                "Account with name=" + account.getAccountName() + " already exists");
        verify(accountRepository, never()).save(any());
    }

    private void verifyAccountWasSavedCorrectly(Account account) {
        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());
        Account savedAccount = accountArgumentCaptor.getValue();
        assertThat(savedAccount.getAccountName()).isEqualTo(account.getAccountName());
        assertThat(savedAccount.getCurrency()).isEqualTo(account.getCurrency());
        assertThat(savedAccount.getTreasury()).isEqualTo(account.getTreasury());
        assertThat(savedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(0));
    }

    private MoveFundsRequest givenNewMoveFundsRequest(Account sourceAccount, Account tagetAccount, BigDecimal amount) {
        return AccountDataFactory.createMoveFundsRequestWithDefaults()
                .sourceAccountId(sourceAccount.getAccountId())
                .targetAccountId(tagetAccount.getAccountId())
                .amount(amount)
                .build();
    }

    private Account givenAccountWithBalance(BigDecimal amount) {
        return AccountDataFactory.acountWithDefaults()
                .balance(amount)
                .build();
    }
}
