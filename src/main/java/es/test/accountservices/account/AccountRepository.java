package es.test.accountservices.account;

import es.test.accountservices.account.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {

    Optional<Account> findByAccountName(String accountName);

}

