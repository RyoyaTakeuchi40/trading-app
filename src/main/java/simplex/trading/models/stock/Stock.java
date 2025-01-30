package simplex.trading.models.stock;

import simplex.trading.models.ProductName;
import simplex.trading.models.Ticker;

/**
 * 株式データ（Stock）：株式に関する情報を保持します。
 *
 * @param ticker       銘柄コード
 * @param productName  銘柄名
 * @param market       上場市場
 * @param sharesIssued 発行済み株式数
 */
public record Stock(
        Ticker ticker,
        ProductName productName,
        MarketEnum market,
        SharesIssued sharesIssued
) {
}