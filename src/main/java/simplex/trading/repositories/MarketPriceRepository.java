package simplex.trading.repositories;

import simplex.trading.exceptions.ValidationException;
import simplex.trading.models.MarketPrice;
import simplex.trading.models.Ticker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class MarketPriceRepository {
    private final String filePath;

    public MarketPriceRepository(String filePath) {
        this.filePath = filePath;
    }

    public Map<Ticker, MarketPrice> loadMarketPrices() {
        Map<Ticker, MarketPrice> marketPrices = new HashMap<>();


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // ヘッダー行の読み飛ばし

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1); // カンマ区切りでフィールドを分割

                if (fields.length != 2) {
                    System.err.println("CSVの形式が不正です。各行は2つのフィールドを持つ必要があります。：" + line);
                    continue;
                }

                try {
                    Ticker ticker = new Ticker(fields[0].trim());
                    MarketPrice marketPrice = new MarketPrice(MarketPrice.parse(fields[1].trim()));

                    marketPrices.put(ticker, marketPrice);
                } catch (ValidationException e) {
                    System.err.println("処理中にエラーが発生しました：" + e.getMessage() + "\n行：" + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ファイルが見つかりません：" + e.getMessage());
        } catch (Exception e) {
            System.err.println("ファイルの読み込み中にエラーが発生しました：" + e.getMessage());
        }

        return marketPrices;
    }
}
