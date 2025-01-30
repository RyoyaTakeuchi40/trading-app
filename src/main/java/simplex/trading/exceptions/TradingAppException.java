package simplex.trading.exceptions;

/**
 * メニュー処理中に想定されたエラーが発生した場合にスローされる例外
 */
public class TradingAppException extends RuntimeException {
    public TradingAppException(String message) {
        super(message);
    }
}