package simplex.trading.menus;


import simplex.trading.exceptions.ValidationException;
import simplex.trading.models.Ticker;
import simplex.trading.models.stock.Stock;

import java.util.Scanner;
import java.util.function.Function;

public class ShowStockDetailMenu extends AbstractMenu {

    public ShowStockDetailMenu() {
        super("銘柄マスタ詳細表示");
    }

    @Override
    public boolean executeInternal() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("取引情報を入力します。");

        Ticker ticker = promptInput(scanner, "銘柄コード", input -> {
            if (stockMap.containsKey(new Ticker(input))) {
                return new Ticker(input);
            }
            throw new ValidationException("銘柄コードが存在しません。");
        });
        Stock stock = stockMap.get(ticker);

        System.out.printf("""
                            　　銘柄コード：%s
                            　　　　銘柄名：%s
                            　　　上場市場：%s
                            発行済み株式数：%,d株
                            　　　　　株価：%s
                        """,
                stock.ticker().value(),
                stock.productName().value(),
                stock.market().getDescription(),
                stock.sharesIssued().value(),
                marketPriceMap.containsKey(ticker) ? String.format("%.2f円", marketPriceMap.get(ticker).value()) : "N/A"
        );


        return true;
    }

    /**
     * 入力リトライの共通ロジック
     *
     * @param scanner Scanner インスタンス
     * @param prompt  入力を促すメッセージ
     * @param parser  入力値をパースして Value Object を生成する関数
     * @param <T>     Value Object の型
     * @return パース済みの Value Object
     */
    private <T> T promptInput(Scanner scanner, String prompt, Function<String, T> parser) {
        while (true) {
            System.out.print(prompt + " > ");
            String input = scanner.nextLine().trim();
            try {
                return parser.apply(input);
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("整数で入力してください。");
            }
        }
    }
}
