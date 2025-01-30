package simplex.trading.models.trade;

import simplex.trading.exceptions.ValidationException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 取引日時（Traded Datetime）：いつ行われた取引なのか。
 * 取引日時は、単位まで入力できるようにしてください。
 * 取引日時の入力位は、現在時間よりも過去の日時である必要があります。現在時間よりも未来の日時である場合は、不正な入力と取り扱ってください。
 * 取引日時は平日（月曜～金曜）の9:00～15:30の間に改まるもののみを受け付けてください
 */
public record TradedDatetime(LocalDateTime value) {
    private static final String PATTERN = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    public TradedDatetime {
        isValidTradedDatetime(value);
    }

    private static void isValidTradedDatetime(LocalDateTime tradedDatetime) {
        String formatted = tradedDatetime.format(FORMATTER);
        if (tradedDatetime.isAfter(LocalDateTime.now())) {
            throw new ValidationException("取引日時は現在時刻よりも過去の日時で指定してください：" + formatted);
        }

        int hour = tradedDatetime.getHour();
        int minute = tradedDatetime.getMinute();
        if (hour < 9 || (hour == 15 && minute > 30) || hour > 15) {
            throw new ValidationException("取引日時は平日（月曜～金曜）の9:00～15:30の間で指定してください：" + formatted);
        }
        DayOfWeek dayOfWeek = tradedDatetime.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new ValidationException("取引日時は平日（月曜～金曜）の9:00～15:30の間で指定してください：" + formatted);
        }
    }

    public static LocalDateTime parse(String tradedDatetime) {
        try {
            return LocalDateTime.parse(tradedDatetime, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ValidationException("取引日時は" + PATTERN + "で指定してください：" + tradedDatetime);
        }
    }

    @Override
    public String toString() {
        return value.format(FORMATTER);
    }
}