package simplex.trading.menus;

import simplex.trading.exceptions.TradingAppException;
import simplex.trading.services.TableBuilder;

public class UpdateMarketPrice extends AbstractMenu {
    public UpdateMarketPrice() {
        super("時価情報更新");
    }

    @Override
    public boolean executeInternal() throws TradingAppException {
        marketPriceMap = marketPriceRepository.loadMarketPrices();
        System.out.println("時価情報を更新しました。");

        if (marketPriceMap.isEmpty()) {
            System.out.println("時価情報はありません。");
            return true;
        }
        TableBuilder tableBuilder = new TableBuilder();
        tableBuilder.withColumn(TableBuilder.Types.TICKER);
        tableBuilder.withColumn(TableBuilder.Types.NAME);
        tableBuilder.withColumn("Market Price", TableBuilder.Types.DECIMAL);

        for (var marketPrice : marketPriceMap.entrySet()) {
            tableBuilder.addRow(
                    marketPrice.getKey().value(),
                    stockMap.get(marketPrice.getKey()).productName().value(),
                    marketPrice.getValue()
            );
        }

        System.out.println(tableBuilder.build());
        return true;
    }
}
