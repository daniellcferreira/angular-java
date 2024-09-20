package br.com.dio.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import br.com.dio.BasicBasket;
import br.com.dio.Box;
import br.com.dio.dao.BasicBasketDAO;
import br.com.dio.model.StockInfo;

public class BasicBasketService {

    private final BasicBasketDAO dao;
    private final MoneyService moneyService;

    public BasicBasketService(final BasicBasketDAO dao, final MoneyService moneyService) {
        this.dao = dao;
        this.moneyService = moneyService;
    }

    public List<BasicBasket> receive(final Box box) {
        var unitAmount = box.unitPrice();

        var finalPrice = unitAmount.add(unitAmount.multiply(new BigDecimal("0.20")));

        var baskets = Stream.generate(() -> new BasicBasket(box.validate(), finalPrice))
                .limit(box.amount())
                .toList();

        return dao.addBatch(baskets);
    }

    public BigDecimal sold(final int amount) {
        var toSold = dao.remove(amount);
        var total = toSold.stream().map(BasicBasket::price).reduce(BigDecimal.ZERO, BigDecimal::add);

        moneyService.add(total);
        return total;
    }

    public StockInfo getInfo() {
        return dao.getInfo();
    }

}
