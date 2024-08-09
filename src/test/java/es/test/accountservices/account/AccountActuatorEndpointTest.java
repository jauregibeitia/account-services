package es.test.accountservices.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import es.test.accountservices.account.mapper.AccountMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@EnableSpringDataWebSupport
@ExtendWith(SpringExtension.class)
@WebMvcTest
//@ContextConfiguration({"classpath*:spring/applicationContext.xml"})
public class AccountActuatorEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AccountMapper accountMapper;

    @MockBean
    AccountActuatorService accountActuatorService;

    @Test
    public void createNewAccountWithInvalidCurrencyCode_thenExceptionIsThrown() throws Exception {
        //Given
        var createAccountRequestDto = AccountDataFactory.createAccountRequestDtoWithDefaults()
                .currencyCode("XsdfsddsXX")
                .build();
        //Expect
        String error =  mockMvc.perform(post("/accounts/create" )
                .content(toJson(createAccountRequestDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();

        assertTrue(StringUtils.contains(error, "Currency not available"));

    }

    @Test
    public void moveFundsSameAddressForTargetAndSource_thenExceptionIsThrown() throws Exception {
        //Given
        var accountId = randomUUID();
        var moveFundsRequestDto = AccountDataFactory.createMoveFundsRequestDtoWithDefaults()
                .sourceAccountId(accountId)
                .targetAccountId(accountId)
                .build();
        //Expect
        String error =  mockMvc.perform(post("/accounts/move-funds" )
                        .content(toJson(moveFundsRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();

        assertTrue(StringUtils.contains(error, "Source and target accounts are the same"));

    }

    @Test
    public void updateAccountWithInvalidCurrencyCode_thenExceptionIsThrown() throws Exception {
        //Given
        var createAccountRequestDto = AccountDataFactory.createUpdateAccountRequestWithDefaults()
                .currencyCode("XsdfsddsXX")
                .build();
        //Expect
        String error =  mockMvc.perform(post("/accounts/update" )
                        .content(toJson(createAccountRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();

        assertTrue(StringUtils.contains(error, "Currency not available"));

    }

    public static String toJson(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }


}
