package me.sstefani.netprog.webapp.controllers;

import me.sstefani.netprog.webapp.integration.CurrencyRepository;
import me.sstefani.netprog.webapp.integration.RateRepository;
import me.sstefani.netprog.webapp.models.Conversion;
import me.sstefani.netprog.webapp.models.Currency;
import me.sstefani.netprog.webapp.models.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConverterController {

    private final CurrencyRepository currencyRepository;
    private final RateRepository rateRepository;

    private Iterable<Currency> currencies;
    private Conversion conversion;

    @Autowired
    public ConverterController(CurrencyRepository currencyRepository, RateRepository rateRepository) {
        this.currencyRepository = currencyRepository;
        this.rateRepository = rateRepository;
        this.currencies = currencyRepository.findAll();
        this.conversion = new Conversion("EUR", "USD", 0, 0);
    }

    @GetMapping(value = "/")
    public String converter(Model model) {
        model.addAttribute("currencies", currencies);
        if (!model.containsAttribute("conversion"))
            model.addAttribute("conversion", conversion);
        return "converter";
    }

    @PostMapping(value = "/convert")
    public String convert(@ModelAttribute Conversion conversion) {
        double result = conversion.getFromCurrency().equals(conversion.getToCurrency())
                ? conversion.getAmount()
                : convertCurrencies(conversion);

        this.conversion = new Conversion(conversion.getFromCurrency(), conversion.getToCurrency(),
                conversion.getAmount(), result);

        return "redirect:/";
    }

    @GetMapping(value = "/reset")
    public String reset() {
        this.conversion = new Conversion("EUR", "USD", 0, 0);
        return "redirect:/";
    }

    private double convertCurrencies(Conversion conversion) {
        Currency fromCurrency = currencyRepository.findByCode(conversion.getFromCurrency());
        Currency toCurrency = currencyRepository.findByCode(conversion.getToCurrency());

        Rate rate = rateRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency);

        return (double) Math.round(conversion.getAmount() * rate.getRate() * 100d) / 100d;
    }

}
