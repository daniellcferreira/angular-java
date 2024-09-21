package br.com.dio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Stream;

import br.com.dio.dao.BasicBasketDAO;
import br.com.dio.dao.MoneyDAO;
import br.com.dio.service.BasicBasketService;
import br.com.dio.service.MoneyService;

public class Main {

    private final static MoneyService moneyService = new MoneyService(new MoneyDAO());

    private final static BasicBasketService basicBasketService = new BasicBasketService(new BasicBasketDAO(),
            moneyService);

    private final static Scanner scanner = new Scanner(System.in);

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
                default -> System.out.println("Opção invalida. escolha uma das opções do menu.");
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
        var outOfDate = basicBasketService.removeOutOfDate();

        var lost = outOfDate.stream().map(BasicBasket::price).reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.printf("Foram descartadas do estoque %s cestas vencidas, o prejuizo foi de %s \n", outOfDate.size(),
                lost);
    }

    private static void receiveItems() {
        System.out.println("Informe o valor da entrega");
        var price = scanner.nextBigDecimal();
        System.out.println("Informe a quantidade de cestas da entrega");
        var amount = scanner.nextLong();
        System.out.println("Informe a data de vencimento");
        var stringValidate = scanner.next();
        var validate = toLocalDate(stringValidate);
        var box = new Box(amount, validate, price);

        var baskets = basicBasketService.receive(box);

        System.out.printf("Foram adicionadas %s cestas ao estoque\n", baskets.size());
    }

    private static LocalDate toLocalDate(final String date) {
        var splitDate = Stream.of(date.split("/")).mapToInt(Integer::parseInt).toArray();
        return LocalDate.of(splitDate[2], splitDate[1], splitDate[0]);
    }

}
