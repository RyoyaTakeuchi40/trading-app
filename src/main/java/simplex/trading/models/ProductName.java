package simplex.trading.models;

import simplex.trading.exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * 銘柄名（Name）：英字で表記された銘柄名です。
 * 利用できる文字は半角英字（大文字、小文字とも）、半角数字、半角スペース、半角ビリオド（.）、半角カッコ（()）、半角アポストロフィ（'）です。
 */
public record ProductName(String value) {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9 .()']+$");

    public ProductName {
        isValidName(value);
    }

    private static void isValidName(String name) throws ValidationException {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("銘柄名が指定されていません。");
        } else if (!NAME_PATTERN.matcher(name).matches()) {
            throw new ValidationException("利用できる文字は半角英字（大文字、小文字とも）、半角数字、半角スペース、半角ピリオド、半角カッコです。：" + name);
        }
    }
}
