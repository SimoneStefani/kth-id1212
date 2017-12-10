package me.sstefani.netprog.webapp.controllers;

import me.sstefani.netprog.webapp.models.Conversion;
import me.sstefani.netprog.webapp.models.Currency;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ConverterController {

    @GetMapping(value = "/")
    public String converter(Model model) {
        model.addAttribute("currencies", makeCurr());
        model.addAttribute("conversion", new Conversion());
        return "converter";
    }

    @PostMapping(value = "/convert")
    public void convert(@ModelAttribute Conversion conversion, Model model) {
        System.out.println(conversion.toString());
    }

    private List<Currency> makeCurr() {
        List<Currency> currlist = new ArrayList<Currency>();
        currlist.add(new Currency("Euro", "EUR"));
        currlist.add(new Currency("US Dollar", "USD"));
        currlist.add(new Currency("Swedish Crown", "SEK"));

        return currlist;
    }
}
