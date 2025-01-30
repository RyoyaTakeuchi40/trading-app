package simplex.trading.models;

import simplex.trading.exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * 銘柄コード（Ticker）：銘柄ごとに一意に付与されるコードです。
 * 4桁の半角英数字で構成されます。
 * 第1桁お上び第3桁は数字のみが利用できます。
 * 第2桁および第4桁は英字・数字のどちらも利用できます。
 * 利用できる英字は、BEI0QVZを除いた19種類です。
 * 英字はすべて大文字表記を利用します
 */
public record Ticker(String value) {
    private static final Pattern TICKER_PATTERN = Pattern.compile("^[0-9][A-Z0-9][0-9][A-Z0-9]$");
    private static final String INVALID_CHARACTERS = "BEI0QVZ";

    public Ticker {
        isValidTicker(value);
    }

    private static void isValidTicker(String ticker) throws ValidationException {
        if (ticker == null || ticker.isEmpty()) {
            throw new ValidationException("銘柄コードが指定されていません。");
        } else if (ticker.length() != 4) {
            throw new ValidationException("4桁の半角英数字で構成されていません。：" + ticker);
        } else if (!TICKER_PATTERN.matcher(ticker).matches()) {
            if (Character.isLetter(ticker.charAt(1)) && Character.isLetter(ticker.charAt(3))) {
                throw new ValidationException("第1桁および第3桁は数字のみが利用できます。：" + ticker);
            }
            throw new ValidationException("4桁の半角英数字で構成されていません。：" + ticker);
        } else if (INVALID_CHARACTERS.indexOf(ticker.charAt(1)) >= 0) {
            throw new ValidationException(String.format("「%s」は利用できない文字です。：%s", ticker.charAt(1), ticker));
        } else if (INVALID_CHARACTERS.indexOf(ticker.charAt(3)) >= 0) {
            throw new ValidationException(String.format("「%s」は利用できない文字です。：%s", ticker.charAt(3), ticker));
        }
    }
}