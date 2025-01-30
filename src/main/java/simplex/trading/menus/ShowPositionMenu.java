package simplex.trading.menus;


import simplex.trading.models.position.Position;
import simplex.trading.services.TableBuilder;

public class ShowPositionMenu extends AbstractMenu {

    public ShowPositionMenu() {
        super("保有ポジション一覧表示");
    }

    @Override
    public boolean executeInternal() {
        if (positions.isEmpty()) {
            System.out.println("保有ポジションはありません。");
            return true;
        }

        TableBuilder tableBuilder = new TableBuilder();

        tableBuilder.withColumn(TableBuilder.Types.TICKER);
        tableBuilder.withColumn(TableBuilder.Types.NAME);
        tableBuilder.withColumn("Quantity", TableBuilder.Types.NUMBER);
        tableBuilder.withColumn("Average Price", TableBuilder.Types.DECIMAL);
        tableBuilder.withColumn("Realized P/L", TableBuilder.Types.DECIMAL);
        tableBuilder.withColumn("Valuation", TableBuilder.Types.DECIMAL);
        tableBuilder.withColumn("Unrealized P/L", TableBuilder.Types.DECIMAL);

        for (Position position : positions.values()) {
            tableBuilder.addRow(
                    position.ticker().value(),
                    stockMap.get(position.ticker()).productName().value(),
                    position.quantity().value(),
                    position.avgUnitPrice(),
                    position.realizedPnL(),
                    position.valuation(marketPriceMap.get(position.ticker())),
                    position.unrealizedPnL(marketPriceMap.get(position.ticker()))
            );
        }

        System.out.println(tableBuilder.build());
        return true;
    }
}
