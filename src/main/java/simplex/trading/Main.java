package simplex.trading;

import simplex.trading.menus.*;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String... args) {

        System.out.println("株式取引管理システムを開始します。");

        boolean continueApp = true;
        while (continueApp) {
            Map<Integer, MenuCommand> menus = new TreeMap<>(Map.of(
                    1, new ShowStockListMenu(),
                    2, new RegisterStockMenu(),
                    3, new ShowStockDetailMenu(),
                    4, new RegisterTradeMenu(),
                    5, new ShowTradeListMenu(),
                    6, new ShowPositionMenu(),
                    9, new ExitMenu()
            ));

            System.out.println("メニューを選択してください。");
            for (var menu : menus.entrySet()) {
                System.out.println("　　" + menu.getKey() + ": " + menu.getValue().menuName());
            }

            System.out.println();
            System.out.print("入力してください：　");
            try {
                int input = Integer.parseInt(new Scanner(System.in).nextLine());
                MenuCommand selected = menus.get(input);
                if (selected == null) {
                    System.out.println("\"" + input + "\"に対応するメニューはありません。");
                    continue;
                }
                continueApp = selected.execute();
            } catch (NumberFormatException e) {
                System.out.println("数字で入力してください。");
            } finally {
                System.out.println();
            }
        }
    }
}