package simplex.trading.models;

import simplex.trading.exceptions.ValidationException;

import java.math.BigDecimal;


/**
 * 市場価格（Market Price）：取引が行われた時点での市場価格を記録します。
 */
public record MarketPrice(BigDecimal value) {
    public MarketPrice {
        isValidMarketPrice(value);
    }

    private static void isValidMarketPrice(BigDecimal marketPrice) {
        if (marketPrice.scale() > 2) {
            throw new ValidationException("取引単価は小数点以下第2位までの値で指定してください：" + marketPrice);
        }
    }

    public static BigDecimal parse(String marketPrice) {
        return new BigDecimal(marketPrice);
    }
}
