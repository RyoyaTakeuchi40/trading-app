package simplex.trading.repositories;

import simplex.trading.exceptions.ValidationException;
import simplex.trading.models.ProductName;
import simplex.trading.models.Ticker;
import simplex.trading.models.stock.MarketEnum;
import simplex.trading.models.stock.SharesIssued;
import simplex.trading.models.stock.Stock;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StockRepository {
    private final String filePath;

    public StockRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // ヘッダー行の読み飛ばし

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1); // カンマ区切りでフィールドを分割

                if (fields.length != 4) {
                    System.err.println("CSVの形式が不正です。各行は4つのフィールドを持つ必要があります。：" + line);
                    continue;
                }

                try {
                    Ticker ticker = new Ticker(fields[0].trim());
                    ProductName productName = new ProductName(fields[1].trim());
                    MarketEnum market = MarketEnum.valueOf(fields[2].trim());
                    SharesIssued sharesIssued = new SharesIssued(SharesIssued.parse(fields[3].trim()));

                    stocks.add(new Stock(ticker, productName, market, sharesIssued));
                } catch (ValidationException e) {
                    System.err.println("処理中にエラーが発生しました：" + e.getMessage() + "\n行：" + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ファイルが見つかりません：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("ファイルの読み込み中にエラーが発生しました：" + e.getMessage());
        }

        return stocks;
    }

    public void registerStock(Stock stock) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            String line = String.format("%s,%s,%s,%d",
                    stock.ticker().value(),
                    stock.productName().value(),
                    stock.market(),
                    stock.sharesIssued().value());
            pw.println(line);
        } catch (FileNotFoundException e) {
            System.err.println("ファイルが見つかりません：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("ファイルへの書き込み中にエラーが発生しました：" + e.getMessage());
        }
    }
}
