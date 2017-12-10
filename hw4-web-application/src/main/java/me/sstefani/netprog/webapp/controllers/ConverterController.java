package me.sstefani.netprog.webapp.controllers;

import me.sstefani.netprog.webapp.integration.CurrencyRepository;
import me.sstefani.netprog.webapp.models.Conversion;
import me.sstefani.netprog.webapp.models.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ConverterController {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public ConverterController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping(value = "/")
    public String converter(Model model) {
        model.addAttribute("currencies", currencyRepository.findAll());
        model.addAttribute("conversion", new Conversion());
        return "converter";
    }

    @PostMapping(value = "/convert")
    public void convert(@ModelAttribute Conversion conversion, Model model) {
        System.out.println(conversion.toString());
    }

}
