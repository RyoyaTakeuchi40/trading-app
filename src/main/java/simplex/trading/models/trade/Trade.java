package simplex.trading.models.trade;

import simplex.trading.models.Quantity;
import simplex.trading.models.Ticker;

import java.time.LocalDateTime;

/**
 * 取引（Trade）：株式取引の取引情報を保持します。
 *
 * @param tradedDateTime  取引日時
 * @param ticker          銘柄コード
 * @param side            買い／売り
 * @param quantity        数量
 * @param tradedUnitPrice 取引単価
 * @param inputDateTime   入力日時
 */
public record Trade(
        TradedDatetime tradedDateTime,
        Ticker ticker,
        SideEnum side,
        Quantity quantity,
        TradedUnitPrice tradedUnitPrice,
        InputDateTime inputDateTime
) {
    public Trade(
            TradedDatetime tradedDateTime,
            Ticker ticker,
            SideEnum side,
            Quantity quantity,
            TradedUnitPrice tradedUnitPrice
    ) {
        this(tradedDateTime, ticker, side, quantity, tradedUnitPrice, new InputDateTime(LocalDateTime.now()));
    }
}