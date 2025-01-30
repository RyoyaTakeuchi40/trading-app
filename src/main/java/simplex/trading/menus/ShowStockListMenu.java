package simplex.trading.menus;


import simplex.trading.models.MarketPrice;
import simplex.trading.models.stock.Stock;
import simplex.trading.services.TableBuilder;

import java.math.BigDecimal;

public class ShowStockListMenu extends AbstractMenu {

    public ShowStockListMenu() {
        super("銘柄マスタ一覧表示");
    }

    @Override
    public boolean executeInternal() {
        if (stocks.isEmpty()) {
            System.out.println("登録された銘柄はありません。");
            return true;
        }

        TableBuilder tableBuilder = new TableBuilder();

        tableBuilder.withColumn(TableBuilder.Types.TICKER);
        tableBuilder.withColumn(TableBuilder.Types.NAME);
        tableBuilder.withColumn("Market", 8);
        tableBuilder.withColumn("Shares Issued", TableBuilder.Types.NUMBER);
        tableBuilder.withColumn("Market Price", 12, TableBuilder.Types.DECIMAL);

        for (Stock stock : stocks) {
            tableBuilder.addRow(
                    stock.ticker().value(),
                    stock.productName().value(),
                    stock.market().getDescription(),
                    stock.sharesIssued().value(),
                    marketPriceMap.getOrDefault(stock.ticker(), new MarketPrice(BigDecimal.ZERO)).value()
            );
        }

        System.out.println(tableBuilder.build());
        return true;
    }
}
