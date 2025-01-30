package simplex.trading.menus;

import simplex.trading.exceptions.TradingAppException;
import simplex.trading.models.MarketPrice;
import simplex.trading.models.Ticker;
import simplex.trading.models.position.Position;
import simplex.trading.models.stock.Stock;
import simplex.trading.models.trade.Trade;
import simplex.trading.repositories.MarketPriceRepository;
import simplex.trading.repositories.StockRepository;
import simplex.trading.repositories.TradeRepository;
import simplex.trading.services.PositionCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractMenu implements MenuCommand {
    static final String STOCKS_CSV_FILE = "files/stocks.csv";
    static final String TRANSACTIONS_CSV_FILE = "files/trade.csv";
    static final String MARKET_PRICE_CSV_FILE = "files/market_prices.csv";
    private final String menuName;

    StockRepository stockRepository = new StockRepository(STOCKS_CSV_FILE);
    TradeRepository tradeRepository = new TradeRepository(TRANSACTIONS_CSV_FILE);
    MarketPriceRepository marketPriceRepository = new MarketPriceRepository(MARKET_PRICE_CSV_FILE);

    List<Stock> stocks = stockRepository.getAllStocks();
    Map<Ticker, Stock> stockMap = createStocksMap(stocks);
    List<Trade> trades = tradeRepository.getAllTrades();
    Map<Ticker, Position> positions = PositionCalculator.calculatePositions(trades);
    Map<Ticker, MarketPrice> marketPriceMap = marketPriceRepository.loadMarketPrices();

    public AbstractMenu(String menuName) {
        this.menuName = menuName;
    }

    static Map<Ticker, Stock> createStocksMap(List<Stock> stocks) {
        Map<Ticker, Stock> stocksMap = new HashMap<>();
        for (Stock stock : stocks) {
            stocksMap.put(stock.ticker(), stock);
        }
        return stocksMap;
    }

    @Override
    public String menuName() {
        return menuName;
    }

    /**
     * 例外ハンドリングを共通化するため
     *
     * @return メニュー選択を継続する場合はtrue, 終了する場合はfalse
     */
    @Override
    public boolean execute() throws TradingAppException {
        try {
            return executeInternal();
        } catch (TradingAppException e) {
            System.out.println("アプリケーションの実行が中断されました。" + e);
            return true;
        }
    }

    /**
     * メニューを実行する
     *
     * @return メニュー選択を継続する場合はtrue, 終了する場合はfalse
     * @throws TradingAppException 例外
     */
    public abstract boolean executeInternal() throws TradingAppException;

}
