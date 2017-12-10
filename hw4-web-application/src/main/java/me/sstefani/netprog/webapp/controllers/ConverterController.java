package me.sstefani.netprog.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ConverterController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String converter(Model model) {
        return "converter";
    }
}
