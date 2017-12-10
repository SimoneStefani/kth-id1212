package me.sstefani.netprog.webapp.integration;

import me.sstefani.netprog.webapp.models.Currency;
import me.sstefani.netprog.webapp.models.Rate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RateRepository extends CrudRepository<Rate, Long> {
    public Rate findByFromCurrencyAndToCurrency(@Param("fromCurrency") Currency fromCurrency, @Param("toCurrency") Currency toCurrency);
}
