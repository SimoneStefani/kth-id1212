package me.sstefani.netprog.webapp;

import me.sstefani.netprog.webapp.integration.CurrencyRepository;
import me.sstefani.netprog.webapp.integration.RateRepository;
import me.sstefani.netprog.webapp.models.Currency;
import me.sstefani.netprog.webapp.models.Rate;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final CurrencyRepository currencyRepository;
    private final RateRepository rateRepository;

    @Autowired
    public Application(CurrencyRepository currencyRepository, RateRepository rateRepository) {
        this.currencyRepository = currencyRepository;
        this.rateRepository = rateRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        persistCurrencies();
        persistRates();
    }

    private void persistCurrencies() {
        String[] currencyCodes = {
                "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK",
                "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN",
                "RON", "RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"};

        for (String code : currencyCodes) {
            currencyRepository.save(new Currency(code));
        }
    }

    private void persistRates() throws IOException {
        for (Currency currency : currencyRepository.findAll()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://api.fixer.io/latest?base=" + currency.getCode()).build();
            Response response = client.newCall(request).execute();

            JSONObject obj = new JSONObject(response.body().string());
            JSONObject rates = obj.getJSONObject("rates");
            for (String key: rates.keySet()) {
                rateRepository.save(new Rate(rates.getDouble(key), currency, currencyRepository.findByCode(key)));
            }
        }
    }
}
