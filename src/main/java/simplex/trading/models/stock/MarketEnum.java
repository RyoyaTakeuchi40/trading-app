package simplex.trading.models.stock;

import simplex.trading.exceptions.ValidationException;

/**
 * 上場市場（Market）：東京証券取引所のどの市場に上場されているかを表します。
 * 本課題では東京証券取引所に上場されている銘柄のみを扱うものとします。
 * プライム、スタンダード、グロースの3種類のいずれかの値をとります
 */
public enum MarketEnum {
    P("Prime"), S("Standard"), G("Growth");

    private final String description;

    MarketEnum(String description) {
        this.description = description;
    }

    public static MarketEnum fromString(String value) {
        return switch (value) {
            case "Prime" -> P;
            case "Standard" -> S;
            case "Growth" -> G;
            default ->
                    throw new ValidationException("上場市場はPrime, Standard, Growthのいずれかを指定してください。：" + value);
        };
    }

    public String getDescription() {
        return description;
    }
}