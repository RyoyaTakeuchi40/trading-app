package simplex.trading.models.trade;

import simplex.trading.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 入力日時 （Input Datetime）：このデータが入力された時刻を記録します。
 * 入力日時は、この取引入力が行われたときの現在時刻を記録してください。
 */
public record InputDateTime(LocalDateTime value) {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    public static InputDateTime parse(String inputDateTime) {
        try {
            return new InputDateTime(LocalDateTime.parse(inputDateTime, FORMATTER));
        } catch (DateTimeParseException e) {
            throw new ValidationException("入力日時は" + PATTERN + "で指定してください：" + inputDateTime);
        }

    }

    @Override
    public String toString() {
        return value.format(FORMATTER);
    }
}
