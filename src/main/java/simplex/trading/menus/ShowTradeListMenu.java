package simplex.trading.menus;


import simplex.trading.models.Ticker;
import simplex.trading.models.trade.Trade;
import simplex.trading.services.TableBuilder;

import java.util.Comparator;

public class ShowTradeListMenu extends AbstractMenu {

    public ShowTradeListMenu() {
        super("取引履歴表示");
    }

    @Override
    public boolean executeInternal() {
        if (trades.isEmpty()) {
            System.out.println("取引履歴はありません。");
            return true;
        }

        TableBuilder tableBuilder = new TableBuilder();

        tableBuilder.withColumn("Traded Datetime", 16);
        tableBuilder.withColumn(TableBuilder.Types.TICKER);
        tableBuilder.withColumn(TableBuilder.Types.NAME);
        tableBuilder.withColumn("Side", 4);
        tableBuilder.withColumn("Quantity", TableBuilder.Types.NUMBER);
        tableBuilder.withColumn("Traded Unit Price", TableBuilder.Types.DECIMAL);

        trades.sort(Comparator.comparing((Trade trade) -> trade.tradedDateTime().value()).reversed());
        for (Trade trade : trades) {
            Ticker ticker = trade.ticker();
            tableBuilder.addRow(
                    trade.tradedDateTime().toString(),
                    ticker.value(),
                    stockMap.get(ticker).productName().value(),
                    trade.side(),
                    trade.quantity().value(),
                    trade.tradedUnitPrice().value()
            );
        }

        System.out.println(tableBuilder.build());
        return true;
    }
}
