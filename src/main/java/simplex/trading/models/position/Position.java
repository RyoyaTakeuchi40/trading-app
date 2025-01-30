package simplex.trading.models.position;

import simplex.trading.models.MarketPrice;
import simplex.trading.models.Quantity;
import simplex.trading.models.Ticker;
import simplex.trading.models.trade.TradedUnitPrice;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ポジション（Position）：保有している銘柄の数量を表します。
 * 算出結果は、小数点第3位を四捨五入し、小数点第2位までを保持するようにしてください。
 * 算出に際して、浮動小数点の演算による精度誤差が生じないように実装してください。
 * 平均取得単価の算出には移動平均法を採用してください。
 * 時価が取得できない銘柄に関しては、評価額と評価損益は算出不可とし、NAと表示してください。
 * クローズされたポジションの平均取得単価は N/A としてください。
 *
 * @param ticker       銘柄
 * @param quantity     保有数量
 * @param avgUnitPrice 平均取得単価
 * @param realizedPnL  実現損益
 */
public record Position(
        Ticker ticker,
        Quantity quantity,
        BigDecimal avgUnitPrice,
        BigDecimal realizedPnL
) {
    public Position {
        if (quantity.value() == 0) {
            avgUnitPrice = BigDecimal.ZERO;
        }
    }

    public Position buy(Quantity newQuantity, TradedUnitPrice price) {
        BigDecimal totalCost = avgUnitPrice.multiply(BigDecimal.valueOf(quantity.value()))
                .add(price.value().multiply(BigDecimal.valueOf(newQuantity.value())));
        Quantity updatedQuantity = new Quantity(quantity.value() + newQuantity.value());
        if (updatedQuantity.value() == 0) {
            return new Position(ticker, updatedQuantity, BigDecimal.ZERO, realizedPnL);
        }
        BigDecimal updatedAvgUnitPrice = totalCost.divide(BigDecimal.valueOf(updatedQuantity.value()), 2, RoundingMode.HALF_UP);
        return new Position(ticker, updatedQuantity, updatedAvgUnitPrice, realizedPnL);
    }

    public Position sell(Quantity newQuantity, TradedUnitPrice price) {
        BigDecimal proceeds = price.value().multiply(BigDecimal.valueOf(newQuantity.value()));
        BigDecimal cost = avgUnitPrice.multiply(BigDecimal.valueOf(newQuantity.value()));
        Quantity updatedQuantity = new Quantity(quantity.value() - newQuantity.value());
        BigDecimal updatedRealizedPnL = realizedPnL.add(proceeds.subtract(cost));
        return new Position(ticker, updatedQuantity, avgUnitPrice, updatedRealizedPnL);
    }

    public BigDecimal valuation(MarketPrice marketPrice) {
        if (marketPrice == null) {
            return BigDecimal.ZERO; // 時価が取得できない場合
        }
        BigDecimal valuation = marketPrice.value().multiply(BigDecimal.valueOf(quantity.value()));
        return valuation.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal unrealizedPnL(MarketPrice marketPrice) {
        if (marketPrice == null) {
            return BigDecimal.ZERO; // 時価が取得できない場合
        }
        BigDecimal unrealizedPnL = marketPrice.value().subtract(avgUnitPrice).multiply(BigDecimal.valueOf(quantity.value()));
        return unrealizedPnL.setScale(2, RoundingMode.HALF_UP);
    }
}