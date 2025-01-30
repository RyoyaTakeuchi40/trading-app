package simplex.trading.services;

import simplex.trading.models.Quantity;
import simplex.trading.models.Ticker;
import simplex.trading.models.position.Position;
import simplex.trading.models.trade.SideEnum;
import simplex.trading.models.trade.Trade;
import simplex.trading.models.trade.TradedUnitPrice;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionCalculator {

    public static Map<Ticker, Position> calculatePositions(List<Trade> trades) {
        Map<Ticker, Position> positions = new HashMap<>();

        trades.sort(Comparator.comparing((Trade t) -> t.tradedDateTime().value()));
        for (Trade Trade : trades) {
            Ticker ticker = Trade.ticker();
            Quantity quantity = Trade.quantity();
            TradedUnitPrice price = Trade.tradedUnitPrice();

            Position position = positions.getOrDefault(ticker, new Position(ticker, new Quantity(0), BigDecimal.ZERO, BigDecimal.ZERO));

            if (Trade.side().equals(SideEnum.BUY)) {
                Position updatedPosition = position.buy(quantity, price);
                positions.put(ticker, updatedPosition);
            } else if (Trade.side().equals(SideEnum.SELL)) {
                Position updatedPosition = position.sell(quantity, price);
                positions.put(ticker, updatedPosition);
            }
        }

        return positions;
    }

    public Map<Ticker, Quantity> getHoldingQuantity(List<Trade> Trades) {
        Map<Ticker, Quantity> holdings = new HashMap<>();

        for (Trade Trade : Trades) {
            Quantity quantity = Trade.quantity();
            Ticker ticker = Trade.ticker();
            Quantity existingQuantity = holdings.getOrDefault(ticker, new Quantity(0));

            // 「買い」取引の場合、数量を加算
            if (Trade.side().equals(SideEnum.BUY)) {
                holdings.put(ticker, new Quantity(existingQuantity.value() + quantity.value()));
            }
            // 「売り」取引の場合、数量を減算
            else if (Trade.side().equals(SideEnum.SELL)) {
                holdings.put(ticker, new Quantity(existingQuantity.value() - quantity.value()));
            }
        }

        return holdings;
    }
}