package es.test.accountservices.account;

import es.test.accountservices.account.dto.AccountDto;
import es.test.accountservices.account.dto.CreateAccountRequestDto;
import es.test.accountservices.account.dto.MoveFundsRequestDto;
import es.test.accountservices.account.dto.UpdateAccountRequestDto;
import es.test.accountservices.account.mapper.AccountMapper;
import es.test.accountservices.account.model.CreateAccountRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, path = "/accounts")
public class AccountActuatorEndpoint {

    private final AccountActuatorService accountActuatorService;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountActuatorEndpoint(AccountActuatorService accountActuatorService, AccountMapper accountMapper) {
        this.accountActuatorService = accountActuatorService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/{accountId}")
    public AccountDto findAccountById(@PathVariable UUID accountId) {
        return accountMapper.accountToDto(accountActuatorService.getAccountById(accountId));
    }

    @GetMapping
    public List<AccountDto> findAllAccounts() {
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
    @ResponseBody
    @ResponseStatus(CREATED)
    public AccountDto createAccount(@Valid @RequestBody CreateAccountRequestDto createAccountRequestDto) {
        CreateAccountRequest createAccountRequest = accountMapper.createAccountRequestDtoToModel(createAccountRequestDto);

        var accountCreated = accountActuatorService.createAccount(createAccountRequest);

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
    @PostMapping("move-funds")
    @ResponseStatus(OK)
    public AccountDto moveFunds(@Valid @RequestBody MoveFundsRequestDto moveFundsRequestDto) throws JSONException {
        var moveFundsRequest = accountMapper.moveFundsRequestDtoToModel(moveFundsRequestDto);
        return accountMapper.accountToDto(accountActuatorService.moveFunds(moveFundsRequest));

    }

    @PostMapping("update")
    @ResponseStatus(OK)
    public AccountDto moveFunds(@Valid @RequestBody UpdateAccountRequestDto updateAccountRequestDto) throws JSONException {
        var updateAccountRequest = accountMapper.updateAccountRequestDtoToModel(updateAccountRequestDto);
        return accountMapper.accountToDto(accountActuatorService.updateAccount(updateAccountRequest));

    }
}
