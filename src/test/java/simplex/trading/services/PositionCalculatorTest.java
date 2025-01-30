package simplex.trading.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import simplex.trading.models.Quantity;
import simplex.trading.models.Ticker;
import simplex.trading.models.position.Position;
import simplex.trading.models.trade.SideEnum;
import simplex.trading.models.trade.Trade;
import simplex.trading.models.trade.TradedDatetime;
import simplex.trading.models.trade.TradedUnitPrice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PositionCalculatorTest {

    @Test
    @DisplayName("Calculate positions")
    void testCalculatePositions() {
        Ticker ticker1 = new Ticker("7203");
        Ticker ticker2 = new Ticker("8306");
        LocalDateTime baseDatetime =LocalDateTime.of(2025, 1, 30, 12, 0);

        Trade trade1 = new Trade(new TradedDatetime(baseDatetime.minusDays(1)), ticker1, SideEnum.BUY, new Quantity(100), new TradedUnitPrice(BigDecimal.valueOf(150)));
        Trade trade2 = new Trade(new TradedDatetime(baseDatetime), ticker1, SideEnum.SELL, new Quantity(100), new TradedUnitPrice(BigDecimal.valueOf(155)));
        Trade trade3 = new Trade(new TradedDatetime(baseDatetime.minusDays(2)), ticker2, SideEnum.BUY, new Quantity(200), new TradedUnitPrice(BigDecimal.valueOf(1000)));

        List<Trade> trades = Arrays.asList(trade1, trade2, trade3);

        Map<Ticker, Position> positions = PositionCalculator.calculatePositions(trades);

        assertEquals(0, positions.get(ticker1).quantity().value());
        assertEquals(200, positions.get(ticker2).quantity().value());
    }

    @Test
    @DisplayName("Get holding quantity")
    void testGetHoldingQuantity() {
        Ticker ticker1 = new Ticker("7203");
        Ticker ticker2 = new Ticker("8306");
        LocalDateTime baseDatetime =LocalDateTime.of(2025, 1, 30, 12, 0);

        Trade trade1 = new Trade(new TradedDatetime(baseDatetime.minusDays(1)), ticker1, SideEnum.BUY, new Quantity(100), new TradedUnitPrice(BigDecimal.valueOf(150)));
        Trade trade2 = new Trade(new TradedDatetime(baseDatetime), ticker1, SideEnum.SELL, new Quantity(100), new TradedUnitPrice(BigDecimal.valueOf(155)));
        Trade trade3 = new Trade(new TradedDatetime(baseDatetime.minusDays(2)), ticker2, SideEnum.BUY, new Quantity(200), new TradedUnitPrice(BigDecimal.valueOf(1000)));

        List<Trade> trades = Arrays.asList(trade1, trade2, trade3);

        Map<Ticker, Quantity> holdings = new PositionCalculator().getHoldingQuantity(trades);

        assertEquals(0, holdings.get(ticker1).value());
        assertEquals(200, holdings.get(ticker2).value());
    }
}