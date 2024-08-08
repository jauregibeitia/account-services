package es.test.accountservices.account;

import es.test.accountservices.account.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends MongoRepository<Account, UUID> {

    Optional<Account> findByAccountName(String accountName);

}

