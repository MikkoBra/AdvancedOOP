package nl.rug.aoop.stockexchangecore.containers;

import lombok.Getter;
import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds information about the stock market and is used for sending this information over the Network.
 */
public class NetworkStockContainer {
    @Getter
    private Map<String, NetworkStock> stocks;

    /**
     * Basic Constructor.
     *
     * @param stocks map containing information about the stock market.
     */
    public NetworkStockContainer(Map<String, NetworkStock> stocks) {
        this.stocks = stocks;
    }

    /**
     * Empty Constructor, the field stock stays null.
     */
    public NetworkStockContainer() {
    }

    /**
     * Method for retrieving all stocks in the stock exchange with a current price lower than a passed parameter value.
     *
     * @param price Double denoting the upper limit of the prices of stocks to be returned.
     * @return A map containing all Stock objects in the stocks map field with a lower price than indicated as values,
     * and their respective symbols as keys.
     */
    public Map<String, NetworkStock> getStocksForPrice(double price) {
        Map<String, NetworkStock> cheaperStocks = new HashMap<>();
        for (Map.Entry<String, NetworkStock> entry : stocks.entrySet()) {
            NetworkStock stock = entry.getValue();
            if (stock.getPrice() <= price) {
                cheaperStocks.put(stock.getSymbol(), stock);
            }
        }
        return cheaperStocks;
    }

    /**
     * Used for updating information that the Trader has.
     *
     * @param stocks map containing information on stock market.
     */
    public void update(Map<String, NetworkStock> stocks) {
        this.stocks = stocks;
    }
}
