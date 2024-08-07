package es.test.accountservices.account;

import es.test.accountservices.account.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends MongoRepository<Account, UUID> {

    Optional<Account> findByAccountName(String accountName);

}

