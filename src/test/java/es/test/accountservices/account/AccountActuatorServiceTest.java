package es.test.accountservices.account;


import es.test.accountservices.account.client.ExchangeRateHttpClient;
import es.test.accountservices.account.exception.AccountNameAlreadyExistsException;
import es.test.accountservices.account.exception.AccountNotFoundException;
import es.test.accountservices.account.model.Account;
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
public class AccountActuatorServiceTest {

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
    void moveFunds_success() {


    }

    private void verifyAccountWasSavedCorrectly(Account account) {
        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());
        Account savedAccount = accountArgumentCaptor.getValue();
        assertThat(savedAccount.getAccountName()).isEqualTo(account.getAccountName());
        assertThat(savedAccount.getCurrency()).isEqualTo(account.getCurrency());
        assertThat(savedAccount.getTreasury()).isEqualTo(account.getTreasury());
        assertThat(savedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(0));
    }

}
