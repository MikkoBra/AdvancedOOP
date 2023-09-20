package nl.rug.aoop.stockexchange;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.containers.TraderContainer;
import nl.rug.aoop.stocks.Stock;
import nl.rug.aoop.trader.TraderInfo;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * StockExchangeFactory is a class that contains functionality for creating a StockExchange object either manually,
 * or through a .yaml file.
 */
@Slf4j
public class StockExchangeFactory {

    /**
     * Method for creating a StockExchange object.
     *
     * @param type String indicating whether to initialize from "manual" or "yaml".
     * @return The created StockExchange object
     * @throws IllegalArgumentException When the passed String matches none of the initialization types.
     * @throws IOException When something goes wrong in initializing from yaml.
     */
    public StockExchange initialize(String type) throws IllegalArgumentException, IOException {
        StockExchange stockExchange;
        if (type.equalsIgnoreCase("manual")) {
            stockExchange = new StockExchange(new StockContainer(), new TraderContainer());
        } else if (type.equalsIgnoreCase("yaml")) {
            stockExchange = initFromYaml();
        } else {
            throw new IllegalArgumentException("Type was not found, stock exchange could not be initialized.");
        }
        return stockExchange;
    }

    private StockExchange initFromYaml() throws IOException {
        TraderContainer traders = initTraderContainer();
        StockContainer stocks = initStockContainer();
        return new StockExchange(stocks, traders);
    }

    private TraderContainer initTraderContainer() throws IOException {
        YamlLoader traderLoader = new YamlLoader(Paths.get("data/traders.yaml"));
        List<Map<String, Object>> traderList = traderLoader.load(List.class);
        TraderContainer traderContainer = new TraderContainer();
        for (Map<String, Object> map : traderList) {
            String id = (String) map.get("id");
            int funds = (Integer) map.get("funds");
            String name = (String) map.get("name");
            Map<String, Integer> portfolio = (Map<String, Integer>) map.get("stockPortfolio");
            TraderInfo trader = new TraderInfo(id, name, funds, portfolio);
            traderContainer.addEntry(trader);
        }
        return traderContainer;
    }

    private StockContainer initStockContainer() throws IOException {
        YamlLoader stockLoader = new YamlLoader(Paths.get("data/stocks.yaml"));
        Map<String, Map<String, Object>> stocks = stockLoader.load(Map.class);
        StockContainer stockContainer = new StockContainer();
        for (Map.Entry<String, Map<String, Object>> entry : stocks.entrySet()) {
            Map<String, Object> map = entry.getValue();
            String symbol = (String) map.get("symbol");
            String name = (String) map.get("name");
            long shares = ((Number) map.get("sharesOutstanding")).longValue();
            double price = (double) map.get("initialPrice");
            Stock stock = new Stock(symbol, name, shares, price);
            stockContainer.addEntry(stock);
        }
        return stockContainer;
    }

}
