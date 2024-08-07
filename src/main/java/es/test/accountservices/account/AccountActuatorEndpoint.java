package es.test.accountservices.account;

import es.test.accountservices.account.dto.AccountDto;
import es.test.accountservices.account.dto.CreateAccountRequestDto;
import es.test.accountservices.account.dto.MoveFundsRequestDto;
import es.test.accountservices.account.mapper.AccountMapper;
import es.test.accountservices.account.model.Account;
import es.test.accountservices.account.model.CreateAccountRequest;
import es.test.accountservices.account.model.MoveFundsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@Component
@RestControllerEndpoint(id = "accounts")
public class AccountActuatorEndpoint {

    private final AccountActuatorService accountActuatorService;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountActuatorEndpoint(AccountActuatorService accountActuatorService, AccountMapper accountMapper) {
        this.accountActuatorService = accountActuatorService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/{id}")
    public AccountDto findAccountById(@PathVariable("id") UUID accountId) {
        return accountMapper.accountToDto(accountActuatorService.getAccountById(accountId));
    }

    @GetMapping
    public List<AccountDto> findAccounts() {
        return accountActuatorService.getAllAccounts()
                .stream().map(accountMapper::accountToDto)
                .toList();
    }
    @Operation(description = "Create an account",
            responses = {
                    @ApiResponse(responseCode = "201", description = "The account has been created", content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class)
                            )
                    })})
    @PostMapping("create")
    @ResponseStatus(CREATED)
    public AccountDto createAccount(@Valid @RequestBody CreateAccountRequestDto createAccountRequestDto) {
        CreateAccountRequest createAccountRequest = accountMapper.createAccountRequestDtoToModel(createAccountRequestDto);

        Account accountCreated = accountActuatorService.createAccount(createAccountRequest);

        return accountMapper.accountToDto(accountCreated);

    }

    @Operation(description = "Move funds from target account to target account",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Funds moved successfully", content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class)
                            )
                    })})
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public AccountDto moveFunds(@Valid @RequestBody MoveFundsRequestDto moveFundsRequestDto) {
        MoveFundsRequest moveFundsRequest = accountMapper.moveFundsRequestDtoToModel(moveFundsRequestDto);
        return accountMapper.accountToDto(accountActuatorService.moveFunds(moveFundsRequest));

    }
}
