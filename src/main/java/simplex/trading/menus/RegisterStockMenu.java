package simplex.trading.menus;

import simplex.trading.exceptions.ValidationException;
import simplex.trading.models.ProductName;
import simplex.trading.models.Ticker;
import simplex.trading.models.stock.MarketEnum;
import simplex.trading.models.stock.SharesIssued;
import simplex.trading.models.stock.Stock;

import java.util.Scanner;
import java.util.function.Function;

public class RegisterStockMenu extends AbstractMenu {
    public RegisterStockMenu() {
        super("銘柄マスタ新規登録");
    }


    @Override
    public boolean executeInternal() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("新規株式銘柄マスタを登録します。");

        Ticker ticker = promptInput(scanner, "銘柄コード", input -> {
            // 重複がないかチェック
            if (!stockMap.containsKey(new Ticker(input))) {
                return new Ticker(input);
            }
            throw new ValidationException("既に登録されている銘柄コードです。");
        });

        ProductName productName = promptInput(scanner, "銘柄名", ProductName::new);

        MarketEnum market = promptInput(scanner, "上場市場 (Prime, Standard, Growth)", MarketEnum::fromString);

        SharesIssued sharesIssued = promptInput(scanner, "発行済み株式数 (1 ～ 999,999,999,999)", input -> new SharesIssued(SharesIssued.parse(input)));

        // Stock オブジェクトを作成し、リポジトリに登録
        Stock stock = new Stock(ticker, productName, market, sharesIssued);
        stockRepository.registerStock(stock);

        System.out.println("新しい銘柄を登録しました。");

        //　銘柄マスタを再読み込み
        stocks = stockRepository.getAllStocks();
        stockMap = createStocksMap(stocks);

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
