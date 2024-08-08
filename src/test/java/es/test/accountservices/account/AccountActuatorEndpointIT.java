package es.test.accountservices.account;

import es.test.accountservices.account.exception.AccountNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountActuatorEndpointIT {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountActuatorService accountActuatorService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        cleanUp();
    }

    @AfterEach
    void cleanUp() {
        accountRepository.deleteAll();
    }

    @Test
    public void accountActuatorEndpoint_givenExistingAccountId_thenAccountIsReturned() throws Exception {
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
    public void accountActuatorEndpoint_givenNonExistingAccountId_thenExceptionIsThrown() throws Exception {
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

}
