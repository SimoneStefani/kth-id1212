package me.sstefani.netprog.webapp.integration;

import me.sstefani.netprog.webapp.models.Rate;
import org.springframework.data.repository.CrudRepository;

public interface RateRepository extends CrudRepository<Rate, Long> {
}
