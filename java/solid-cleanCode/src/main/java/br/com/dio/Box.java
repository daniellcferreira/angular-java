package br.com.dio;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.RoundingMode.CEILING;

public record Box(long amount, LocalDate validate, BigDecimal price) {

    public BigDecimal unitPrice() {
        return price().divide(new BigDecimal(amount()), CEILING);
    }
}
