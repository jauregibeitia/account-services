package es.test.accountservices.account.mapper;

import es.test.accountservices.account.dto.AccountDto;
import es.test.accountservices.account.dto.CreateAccountRequestDto;
import es.test.accountservices.account.dto.MoveFundsRequestDto;
import es.test.accountservices.account.model.Account;
import es.test.accountservices.account.model.CreateAccountRequest;
import es.test.accountservices.account.model.MoveFundsRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDto accountToDto(Account account) {
        return new AccountDto(account.getAccountId(),
                account.getAccountName(),
                account.getCurrency(),
                account.getBalance(),
                account.getTreasury()

        );
    }

    public CreateAccountRequest createAccountRequestDtoToModel(CreateAccountRequestDto createAccountRequestDto) {
        return new CreateAccountRequest(createAccountRequestDto.getAccountName(),
                createAccountRequestDto.getCurrency(),
                createAccountRequestDto.getTreasury());
    }

    public AccountDto accountDtoToModel(Account account) {
        return new AccountDto(account.getAccountId(),
                account.getAccountName(),
                account.getCurrency(),
                account.getBalance(),
                account.getTreasury()

        );
    }

        public MoveFundsRequest moveFundsRequestDtoToModel(MoveFundsRequestDto moveFundsRequestDto) {
        return new MoveFundsRequest(moveFundsRequestDto.getSourceAccountId(),
                moveFundsRequestDto.getTargetAccountId(),
                moveFundsRequestDto.getCurrency(),
                moveFundsRequestDto.getAmount());
    }
}
