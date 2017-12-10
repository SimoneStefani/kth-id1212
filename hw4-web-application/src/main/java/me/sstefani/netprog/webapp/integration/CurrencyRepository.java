package me.sstefani.netprog.webapp.integration;

import me.sstefani.netprog.webapp.models.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Currency findByCode(@Param("code") String code);
}
