package simplex.trading.models.trade;

import simplex.trading.exceptions.ValidationException;

public enum SideEnum {
    BUY("Buy"), SELL("Sell");

    private final String description;

    SideEnum(String description) {
        this.description = description;
    }

    public static SideEnum fromString(String value) {
        return switch (value) {
            case "Buy" -> BUY;
            case "Sell" -> SELL;
            default -> throw new ValidationException("売買区分はBuy, Sellのいずれかを指定してください。：" + value);
        };
    }

    public String getDescription() {
        return description;
    }
}