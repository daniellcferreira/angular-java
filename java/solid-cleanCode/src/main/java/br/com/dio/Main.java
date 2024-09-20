package br.com.dio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.dio.dao.BasicBasketDAO;
import br.com.dio.dao.MoneyDAO;
import br.com.dio.service.BasicBasketService;
import br.com.dio.service.MoneyService;

public class Main {

    private static MoneyService moneyService = new MoneyService(new MoneyDAO());

    private static BasicBasketService basicBasketService = new BasicBasketService(new BasicBasketDAO(), moneyService);

    private final static Scanner scanner = new Scanner(System.in);

    private static List<BasicBasket> stock = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Bem vindo ao sistema de armazém");
        System.out.println("Selecione a opção desejada");
        var option = -1;
        while (true) {
            System.out.println("1 - Verificar estoque de cesta básica");
            System.out.println("2 - Verificar caixa");
            System.out.println("3 - Receber Cestas");
            System.out.println("4 - Vender Cestas");
            System.out.println("5 - Remover itens vencidos");
            System.out.println("6 - Sair");
            option = scanner.nextInt();
            switch (option) {
                case 1 -> checkStock();
                case 2 -> checkMoney();
                case 3 -> receiveItems();
                case 4 -> soldItems();
                case 5 -> removeItemsOutOfDate();
                case 6 -> System.exit(0);
            }
        }
    }

    private static void soldItems() {
        System.out.println("Quantas cestar serão vendidas");
        var amount = scanner.nextInt();
        var total = basicBasketService.sold(amount);
        System.out.printf("O valor da venda é de %s \n", total);
    }

    private static void checkStock() {
        var stockInfo = basicBasketService.getInfo();
        System.out.printf("Existem %s cestas em estoque, das quais %s estão fora do prazo de validade \n",
                stockInfo.total(), stockInfo.outOfDate());
    }

    private static void checkMoney() {
        System.out.printf("O caixa no momento é de %s\n", moneyService.getMoney());
    }

    private static void removeItemsOutOfDate() {
        var outOfDate = stock.stream().filter(b -> b.validate().isBefore(LocalDate.now())).toList();
        var lost = outOfDate.stream().map(BasicBasket::price).reduce(BigDecimal.ZERO, BigDecimal::add);
        stock = stock.stream().filter(b -> b.validate().isBefore(LocalDate.now())).collect(Collectors.toList());
        System.out.printf("Foram descartadas do estoque %s cestas vencidas, o prejuizo foi de %s \n", outOfDate.size(),
                lost);
    }

    private static void receiveItems() {
        System.out.println("Informe o valor da entrega");
        var price = scanner.nextBigDecimal();
        System.out.println("Informe a quantidade de cestas da entrega");
        var amount = scanner.nextLong();
        System.out.println("Informe a data de vencimento");
        var validate = scanner.next();
        var day = Integer.parseInt(validate.split("/")[0]);
        var month = Integer.parseInt(validate.split("/")[1]);
        var year = Integer.parseInt(validate.split("/")[2]);
        var box = new Box(amount, LocalDate.of(year, month, day), price);

        var baskets = basicBasketService.receive(box);

        System.out.printf("Foram adicionadas %s cestas ao estoque\n", baskets.size());
    }

}
