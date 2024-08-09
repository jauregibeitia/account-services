package es.test.accountservices.account.mapper;

import es.test.accountservices.account.dto.AccountDto;
import es.test.accountservices.account.dto.CreateAccountRequestDto;
import es.test.accountservices.account.dto.MoveFundsRequestDto;
import es.test.accountservices.account.dto.UpdateAccountRequestDto;
import es.test.accountservices.account.model.Account;
import es.test.accountservices.account.model.CreateAccountRequest;
import es.test.accountservices.account.model.MoveFundsRequest;
import es.test.accountservices.account.model.UpdateAccountRequest;
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
        return new CreateAccountRequest(createAccountRequestDto.accountName(),
                createAccountRequestDto.currencyCode(),
                createAccountRequestDto.treasury());
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
                moveFundsRequestDto.getAmount());
    }

    public UpdateAccountRequest updateAccountRequestDtoToModel(UpdateAccountRequestDto updateAccountRequestDto) {
        return new UpdateAccountRequest(updateAccountRequestDto.accountId(),
                updateAccountRequestDto.accountName(),
                updateAccountRequestDto.currencyCode());
    }
}
