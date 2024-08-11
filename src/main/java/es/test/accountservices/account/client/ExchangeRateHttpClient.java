package es.test.accountservices.account.client;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Log4j2
@Component
public class ExchangeRateHttpClient {


    private final RestTemplate restTemplate;

    private static String URI = "https://api.exchangerate-api.com/v4/latest/";

    @Autowired
    public ExchangeRateHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getRate(String fromCurrency, String toCurrency) throws JSONException {

        ResponseEntity<String> response
                = restTemplate.getForEntity(URI + fromCurrency, String.class);

        String body = response.getBody().toString();
        JSONObject jsonObject = new JSONObject(body);
        var rates = jsonObject.getJSONObject("rates");

        return BigDecimal.valueOf(rates.getDouble(toCurrency));

    }

}