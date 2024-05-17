import ApiConnection.Api;
import GUI.Menu;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        Api api = new Api();
        Menu menu = new Menu(api);
        menu.mainMenu();
        int option = sc.nextInt();
        while (option < menu.getSize()) {
            if (option >= 1 && option <= menu.getSize() - 2) {
                menu.conversionMenu(sc,option);
            } else {
                menu.customConversionMenu(sc);
            }
            menu.mainMenu();
            option = sc.nextInt();
        }
        sc.close();
    }
}