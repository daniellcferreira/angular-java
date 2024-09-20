package br.com.dio.service;

import java.math.BigDecimal;

import br.com.dio.dao.MoneyDAO;

public class MoneyService {

    private final MoneyDAO dao;

    public MoneyService(final MoneyDAO dao) {
        this.dao = dao;
    }

    public BigDecimal add(final BigDecimal money) {
        return dao.add(money);
    }

    public BigDecimal getMoney() {
        return dao.getMoney();
    }
}
