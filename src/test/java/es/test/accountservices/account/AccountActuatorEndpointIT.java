package es.test.accountservices.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import es.test.accountservices.account.exception.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class AccountActuatorEndpointIT {

    @MockBean
    private AccountActuatorService accountActuatorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenExistingAccountId_thenAccountIsReturned() throws Exception {
        //Given
        var expectedAccount = AccountDataFactory.acountWithDefaults().build();
        given(accountActuatorService.getAccountById(any())).willReturn(expectedAccount);
        //When
        ResultActions result = mockMvc.perform(get("/accounts/" + expectedAccount.getAccountId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON));
        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(expectedAccount.getAccountId().toString()))
                .andExpect(jsonPath("$.accountName").value(expectedAccount.getAccountName()))
                .andExpect(jsonPath("$.currency").value(expectedAccount.getCurrency().getCurrencyCode()))
                .andExpect(jsonPath("$.balance").value(expectedAccount.getBalance()))
                .andExpect(jsonPath("$.treasury").value(expectedAccount.getTreasury()));
    }
    @Test
    public void givenNonExistingAccountId_thenExceptionIsThrown() throws Exception {
        //Given
        var accountId = AccountDataFactory.randomAccountId();
        given(accountActuatorService.getAccountById(accountId)).willThrow(new AccountNotFoundException(accountId));
        //When
        mockMvc.perform(get("/accounts/" + accountId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccountNotFoundException))
                .andExpect(result -> assertEquals("Account with accountId=" + accountId + " not found", result.getResolvedException().getMessage()));
    }
    @Test
    public void givenExistingAccounts_thenAccountsAreReturned() throws Exception {
        //Given
        var account_one = AccountDataFactory.acountWithDefaults().build();
        var account_two = AccountDataFactory.acountWithDefaults().build();
        var account_three = AccountDataFactory.acountWithDefaults().build();
        given(accountActuatorService.getAllAccounts()).willReturn(List.of(account_one, account_two, account_three));
        //When
        ResultActions result = mockMvc.perform(get("/accounts" )
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk());
    }
    @Test
    public void givenNoExistingAccounts_thenEmptyListIsReturned() throws Exception {
        //Given
        given(accountActuatorService.getAllAccounts()).willReturn(Collections.emptyList());
        //When
        ResultActions result = mockMvc.perform(get("/accounts" )
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON));
        // Then
        result.andExpect(status().isOk());
    }

    @Test
    public void createNewAccount_thenNewAccountIsReturned() throws Exception {
        //Given
        var newAccount = AccountDataFactory.acountWithDefaults().build();
        var createAccountRequestDto = AccountDataFactory.createAccountRequestDtoWithDefaults()
                .accountName(newAccount.getAccountName())
                .currencyCode(newAccount.getCurrency().getCurrencyCode())
                .treasury(newAccount.getTreasury())
                .build();

        given(accountActuatorService.createAccount(any())).willReturn(newAccount);
        //When
        ResultActions result = mockMvc.perform(post("/accounts/create" )
                .content(toJson(createAccountRequestDto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(newAccount.getAccountId().toString()))
                .andExpect(jsonPath("$.accountName").value(newAccount.getAccountName()))
                .andExpect(jsonPath("$.currency").value(newAccount.getCurrency().getCurrencyCode()))
                .andExpect(jsonPath("$.balance").value(newAccount.getBalance()))
                .andExpect(jsonPath("$.treasury").value(newAccount.getTreasury()));

    }

    @Test
    public void moveFunds_thenSourceAccountIsReturned() throws Exception {
        //Given
        var sourceAccount = AccountDataFactory.acountWithDefaults()
                .balance(BigDecimal.valueOf(10))
                .build();
        var targetAccount = AccountDataFactory.acountWithDefaults().build();

        var moveFundsRequestDto = AccountDataFactory.createMoveFundsRequestDtoWithDefaults()
                .sourceAccountId(sourceAccount.getAccountId())
                .targetAccountId(targetAccount.getAccountId())
                .amount((BigDecimal.valueOf(8)))
                .build();

        given(accountActuatorService.moveFunds(any())).willReturn(sourceAccount);
        //When
        ResultActions result = mockMvc.perform(post("/accounts/move-funds" )
                .content(toJson(moveFundsRequestDto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());;
        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(sourceAccount.getAccountId().toString()));

    }

    @Test
    public void updateAccount_thenUpdatedAccountIsReturned() throws Exception {
        //Given
        var account = AccountDataFactory.acountWithDefaults().build();
        var updateAccountRequest = AccountDataFactory.createUpdateAccountRequestWithDefaults()
                .accountId(account.getAccountId())
                .build();

        given(accountActuatorService.updateAccount(any())).willReturn(account);
        //When
        ResultActions result = mockMvc.perform(post("/accounts/update" )
                        .content(toJson(updateAccountRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());;
        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(account.getAccountId().toString()));

    }

    public static String toJson(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }


}
