package simplex.trading.models.trade;

import simplex.trading.exceptions.ValidationException;

import java.math.BigDecimal;


/**
 * 取引単価（Traded Unit Price）：1株あたりいくらで取引したのか。
 * 小数点以下第2位まで扱えるように実装してください。
 */
public record TradedUnitPrice(BigDecimal value) {
    public TradedUnitPrice {
        isValidTradedUnitPrice(value);
    }

    private static void isValidTradedUnitPrice(BigDecimal tradedUnitPrice) {
        if (tradedUnitPrice.scale() > 2) {
            throw new ValidationException("取引単価は小数点以下第2位までの値で指定してください：" + tradedUnitPrice);
        }
    }

    public static BigDecimal parse(String tradedUnitPrice) {
        return new BigDecimal(tradedUnitPrice);
    }
}
