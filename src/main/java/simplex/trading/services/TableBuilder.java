package simplex.trading.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TableBuilder {
    private static final int NAME_MAX_LEN = 20;
    List<Column> columns = new ArrayList<>();
    List<List<Object>> rows = new ArrayList<>();

    private String horizontalLine = "-";
    private String verticalLine = "|";


    public TableBuilder() {
    }

    public TableBuilder(String horizontalLine, String verticalLine) {
        this.horizontalLine = horizontalLine;
        this.verticalLine = verticalLine;
    }

    String formatName(String name) {
        if (name.length() > NAME_MAX_LEN) {
            return name.substring(0, NAME_MAX_LEN - 3) + "...";
        }
        return name;
    }

    public void withColumn(Types type) {
        String name = switch (type) {
            case TICKER -> "Ticker";
            case NAME -> "Product Name";
            default -> "";
        };
        int width = switch (type) {
            case TICKER -> 6;
            case NAME -> NAME_MAX_LEN;
            default -> 0;
        };
        columns.add(new Column(name, width, type));
    }

    public void withColumn(String name, Types type) {
        int width = switch (type) {
            case NUMBER -> 14;
            case DECIMAL -> 17;
            default -> 0;
        };
        columns.add(new Column(name, width, type));
    }

    public void withColumn(String name, int width) {
        columns.add(new Column(name, width, Types.STRING));
    }

    public void withColumn(String name, int width, Types type) {
        columns.add(new Column(name, width, type));
    }

    public void addRow(Object... values) {
        rows.add(List.of(values));
    }

    public String build() {
        String border = horizontalLine.repeat(columns.stream().mapToInt(column -> column.width + 3).sum() + 1);

        StringBuilder table = new StringBuilder();

        //　ヘッダー
        table.append(border).append("\n");
        for (Column column : columns) {
            String cell = verticalLine + " %-" + column.width + "s ";
            table.append(String.format(cell, column.name));
        }
        table.append(verticalLine).append("\n");
        table.append(border).append("\n");
        //　ボディ
        for (List<Object> row : rows) {
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                Object value = row.get(i);
                String cell;
                if (column.type == Types.NUMBER) {
                    cell = verticalLine + " %," + column.width + "d ";
                } else if (column.type == Types.DECIMAL) {
                    cell = verticalLine + " %" + column.width + "s ";
                } else {
                    cell = verticalLine + " %-" + column.width + "s ";
                }

                if (column.type == Types.NAME) {
                    value = formatName((String) value);
                } else if (column.type == Types.DECIMAL) {
                    value = formatCurrency((BigDecimal) value);
                }
                table.append(String.format(cell, value));
            }
            table.append(verticalLine).append("\n");
        }
        table.append(border).append("\n");

        return table.toString();
    }

    private String formatCurrency(BigDecimal value) {
        return value.equals(BigDecimal.ZERO) ? "N/A" : String.format("%,.2f", value);
    }

    public enum Types {
        STRING, TICKER, NAME, NUMBER, DECIMAL
    }

    private record Column(String name, int width, Types type) {
    }

}