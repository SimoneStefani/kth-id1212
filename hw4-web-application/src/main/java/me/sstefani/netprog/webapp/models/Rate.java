package me.sstefani.netprog.webapp.models;

import javax.persistence.*;

@Entity
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private double rate;

    @ManyToOne
    private Currency fromCurrency;

    @ManyToOne
    private Currency toCurrency;

    public Rate() {
    }

    public Rate(double rate, Currency fromCurrency, Currency toCurrency) {
        this.rate = rate;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(Currency fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency toCurrency) {
        this.toCurrency = toCurrency;
    }
}
