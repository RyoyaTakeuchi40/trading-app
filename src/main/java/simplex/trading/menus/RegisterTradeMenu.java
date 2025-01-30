package simplex.trading.menus;

import simplex.trading.exceptions.ValidationException;
import simplex.trading.models.Quantity;
import simplex.trading.models.Ticker;
import simplex.trading.models.trade.SideEnum;
import simplex.trading.models.trade.Trade;
import simplex.trading.models.trade.TradedDatetime;
import simplex.trading.models.trade.TradedUnitPrice;

import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Function;

public class RegisterTradeMenu extends AbstractMenu {
    public RegisterTradeMenu() {
        super("取引入力");
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

        TradedDatetime tradedDatetime = promptInput(scanner, "取引日時（YYYY-MM-DD HH:MM）", input -> {
            TradedDatetime inputDatetime = new TradedDatetime(TradedDatetime.parse(input));
            TradedDatetime lastTransactionDatetime = getLastTransactionDatetime(ticker);
            if (lastTransactionDatetime != null && !lastTransactionDatetime.value().isBefore(inputDatetime.value())) {
                throw new ValidationException("同一銘柄の最終取引日時より新しい日時を指定してください。\n最終取引日時：" + lastTransactionDatetime);
            }
            return inputDatetime;
        });

        SideEnum side = promptInput(scanner, "売買区分 (Buy, Sell)", input -> {
            SideEnum inputSide = SideEnum.fromString(input);
            if (inputSide == SideEnum.SELL && !positions.containsKey(ticker)) {
                throw new ValidationException("保有ポジションがありません。");
            }
            return inputSide;
        });

        Quantity quantity = promptInput(scanner, "取引数量", input -> {
            Quantity inputQuantity = new Quantity(Quantity.parse(input));
            if (side == SideEnum.SELL && positions.get(ticker).quantity().value() < inputQuantity.value()) {
                throw new ValidationException("保有数量を超える売却はできません。");
            }
            return inputQuantity;
        });

        TradedUnitPrice tradedUnitPrice = promptInput(scanner, "取引単価", input -> new TradedUnitPrice(TradedUnitPrice.parse(input)));

        Trade trade = new Trade(tradedDatetime, ticker, side, quantity, tradedUnitPrice);
        tradeRepository.registerTrade(trade);

        System.out.println("取引を登録しました。");

        // 再読み込み
        trades = tradeRepository.getAllTrades();

        return true;
    }

    /**
     * @return tradesから同一のtickerの内、最新の取引日時を取得する
     */
    private TradedDatetime getLastTransactionDatetime(Ticker ticker) {
        return trades.stream()
                .filter(t -> t.ticker().equals(ticker))
                .map(Trade::tradedDateTime)
                .max(Comparator.comparing(TradedDatetime::value))
                .orElse(null);
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
