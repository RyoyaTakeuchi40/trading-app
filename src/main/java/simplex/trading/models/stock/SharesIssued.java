package simplex.trading.models.stock;

import simplex.trading.exceptions.ValidationException;

/**
 * 発行済み株式数（Shares Issued）：既に発行された株式数。
 * 整数です。
 * 値の取りうる範囲は1から999,999,999,999までとします。
 */
public record SharesIssued(long value) {
    private static final long MIN_VALUE = 1;
    private static final long MAX_VALUE = 999_999_999_999L;

    public SharesIssued {
        isValidSharesIssued(value);
    }

    private static void isValidSharesIssued(long sharesIssued) throws ValidationException {
        if (sharesIssued < MIN_VALUE || sharesIssued > MAX_VALUE) {
            throw new ValidationException(String.format("値の取りうる範囲は%dから%dまでです。：%d", MIN_VALUE, MAX_VALUE, sharesIssued));
        }
    }

    public static Long parse(String sharesIssued) {
        try {
            return Long.parseLong(sharesIssued);
        } catch (NumberFormatException e) {
            throw new ValidationException("発行済み株式数は整数で指定してください：" + sharesIssued);
        }
    }
}