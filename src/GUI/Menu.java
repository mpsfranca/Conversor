package GUI;

import ApiConnection.Api;
import Currency.Currency;

import java.util.Scanner;

public class Menu {
    private static final String[][] CURRENCY_PAIRS = {
            {"USD", "ARS"},
            {"ARS", "USD"},
            {"USD", "BRL"},
            {"BRL", "USD"},
            {"USD", "COP"},
            {"COP", "USD"}
    };
    private static Api apiConnection;
    private final int size = CURRENCY_PAIRS.length + 2;

    public Menu(Api api) {
        apiConnection = api;
    }

    public void mainMenu() {
        System.out.println("************************************************");
        System.out.println("Seja bem-vindo/a ao Conversor de Moeda =]\n");
        for (int i = 0; i < CURRENCY_PAIRS.length; i++) {
            String c1 = CURRENCY_PAIRS[i][0];
            String c2 = CURRENCY_PAIRS[i][1];
            linhaMenu(i+1, apiConnection.getCurrency(c1), apiConnection.getCurrency(c2));
        }

        System.out.printf("%d) Conversão personalizada\n",CURRENCY_PAIRS.length + 1);
        System.out.printf("%d) Sair\n",CURRENCY_PAIRS.length + 2);
        System.out.println("Escolha uma opção válida:");
        System.out.println("************************************************");
    }

    public void printConversionResult(Scanner sc, Currency c1,Currency c2) {
        Float valueToConvert = sc.nextFloat();
        System.out.printf("Valor %.2f [%s] corresponde ao valor final de =>>> %.4f [%s]\n",valueToConvert,c1.getCode(),apiConnection.getConversionResult(c1,c2,valueToConvert),c2.getCode());
    }

    public void conversionMenu(Scanner sc, int index) {
        System.out.println("Digite o valor que deseja converter:");
        Currency c1 = apiConnection.getCurrency(CURRENCY_PAIRS[index-1][0]);
        Currency c2 = apiConnection.getCurrency(CURRENCY_PAIRS[index-1][1]);
        printConversionResult(sc,c1,c2);
    }

    public void customConversionMenu(Scanner sc) {
        sc.nextLine();
        System.out.println("Digite o código da moeda a converter:");
        Currency c1 = apiConnection.getCurrency(sc.nextLine());
        System.out.println("Digite o código da moeda destino:");
        Currency c2 = apiConnection.getCurrency(sc.nextLine());
        if (c1 == null || c2 == null) {
            System.out.println("Um código informado não é suportado.");
            return;
        }
        System.out.println("Digite o valor a ser convertido:");
        printConversionResult(sc,c1,c2);
    }

    private void linhaMenu(int index, Currency c1, Currency c2) {
        System.out.printf("%d) %s =>> %s\n", index, c1.getName(), c2.getName());
    }

    public int getSize() {
        return size;
    }
}
