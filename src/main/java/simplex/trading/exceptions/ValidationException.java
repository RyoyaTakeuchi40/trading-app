package simplex.trading.exceptions;

/**
 * バリデーションエラーが発生した場合にスローされる例外
 */
public class ValidationException extends IllegalArgumentException {
    public ValidationException(String message) {
        super(message);
    }
}