package simplex.trading.repositories;

import simplex.trading.models.trade.Trade;
import simplex.trading.models.trade.*;
import simplex.trading.models.Ticker;
import simplex.trading.models.Quantity;


import simplex.trading.exceptions.ValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TradeRepository {
    private final String filePath;

    public TradeRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Trade> getAllTrades() {
        List<Trade> trades = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // ヘッダー行の読み飛ばし

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1); // カンマ区切りでフィールドを分割

                if (fields.length != 6) {
                    System.err.println("CSVの形式が不正です。各行は6つのフィールドを持つ必要があります。：" + line);
                    continue;
                }

                try {
                    TradedDatetime tradedDatetime = new TradedDatetime(TradedDatetime.parse(fields[0].trim()));
                    Ticker ticker = new Ticker(fields[1].trim());
                    SideEnum side = SideEnum.valueOf(fields[2].trim());
                    Quantity quantity = new Quantity(Long.parseLong(fields[3].trim()));
                    TradedUnitPrice tradedUnitPrice = new TradedUnitPrice(TradedUnitPrice.parse(fields[4].trim()));
                    InputDateTime inputDateTime = InputDateTime.parse((fields[5].trim()));

                    trades.add(new Trade(tradedDatetime, ticker, side, quantity, tradedUnitPrice, inputDateTime));
                } catch (ValidationException e) {
                    System.err.println("処理中にエラーが発生しました：" + e.getMessage() + "\n行：" + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ファイルが見つかりません：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("ファイルの読み込み中にエラーが発生しました：" + e.getMessage());
        }

        return trades;
    }

    public void registerTrade(Trade trade) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            String line = String.format("%s,%s,%s,%d,%s,%s",
                    trade.tradedDateTime().toString(),
                    trade.ticker().value(),
                    trade.side(),
                    trade.quantity().value(),
                    trade.tradedUnitPrice().value(),
                    trade.inputDateTime().toString());
            pw.println(line);
        } catch (FileNotFoundException e) {
            System.err.println("ファイルが見つかりません：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("ファイルの書き込み中にエラーが発生しました：" + e.getMessage());
        }
    }

}
