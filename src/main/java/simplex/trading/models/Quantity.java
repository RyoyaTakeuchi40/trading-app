package simplex.trading.models;

import simplex.trading.exceptions.ValidationException;

/**
 * 数量（Quantity）：何株取引したのか。東京証券取引所であれば100株準位で取引します。
 */
public record Quantity(long value) {
    public Quantity {
        isValidQuantity(value);
    }

    private static void isValidQuantity(long quantity) {
        if (quantity % 100 != 0) {
            throw new ValidationException("数量は100株単位で指定してください：" + quantity);
        }
    }

    public static Long parse(String quantity) {
        try {
            return Long.parseLong(quantity);
        } catch (NumberFormatException e) {
            throw new ValidationException("数量は整数で指定してください：" + quantity);
        }
    }
}