package es.test.accountservices.account;

import es.test.accountservices.account.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;


import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class AccountRepositoryIT {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void clearDB() {
        accountRepository.deleteAll();
    }

    @Test
    void findById_success() {
        //Given
        var account = givenAccount();
        //When
        var result = accountRepository.findById(account.getAccountId());
        //Then
        assertThat(result).isEqualTo(Optional.of(account));

    }

    @Test
    void findAll_success() {
        //Given
        var account_one = givenAccount();
        var account_two = givenAccount();
        //When
        var result = accountRepository.findAll();
        //Then
        assertThat(result).containsExactlyInAnyOrder(account_one, account_two);

    }

    @Test
    void findByAccountName_success() {
        //Given
        var account = givenAccount();
        //When
        var result = accountRepository.findByAccountName(account.getAccountName());
        //Then
        assertThat(result).contains(account);
    }

    @Test
    void findById_empty() {
        //Given
        var account = AccountDataFactory.acountWithDefaults().build();
        //When
        var result = accountRepository.findById(account.getAccountId());
        //Then
        assertThat(result).isEmpty();

    }

    @Test
    void findAll_empty() {
        //When
        var result = accountRepository.findAll();
        //Then
        assertThat(result).isEmpty();

    }

    @Test
    void findByAccountName_empty() {
        //Given
        var account = AccountDataFactory.acountWithDefaults().build();
        //When
        var result = accountRepository.findByAccountName(account.getAccountName());
        //Then
        assertThat(result).isEmpty();
    }

    private Account givenAccount() {
        var account = AccountDataFactory.acountWithDefaults().build();
        accountRepository.save(account);
        return account;
    }


}