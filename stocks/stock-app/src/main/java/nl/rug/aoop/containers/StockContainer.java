package nl.rug.aoop.containers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;
import nl.rug.aoop.stocks.Stock;

import java.util.HashMap;
import java.util.Map;

/**
 * Class implementing the functionality of a StockContainer object. It keeps track of the info of all stocks in a
 * stock app.
 */
@Slf4j
public class StockContainer extends Container<Stock> {

    @Getter
    private final Map<String, Stock> stocks;

    /**
     * Constructor for initializing stocks from a passed parameter map.
     *
     * @param stocks Map object containing the desired stocks as value, and their respective String symbols as key.
     */
    public StockContainer(Map<String, Stock> stocks) {
        this.stocks = stocks;
    }

    /**
     * Default constructor, initializes the stock map field with an empty HashMap.
     */
    public StockContainer() {
        this(new HashMap<>());
    }

    /**
     * Method that returns a Stock from the stocks map based on its symbol.
     *
     * @param symbol The String symbol belonging to the stock which should be retrieved.
     * @return The stock with the passed symbol.
     */
    @Override
    public Stock getInfo(String symbol) {
        return stocks.get(symbol);
    }

    /**
     * Method for adding a stock to the stock exchange.
     *
     * @param stock Stock object to be added to the stocks map.
     */
    @Override
    public void addEntry(Stock stock) {
        stocks.put(stock.getSymbol(), stock);
    }

    /**
     * Method for removing a stock from the stock exchange.
     *
     * @param stockId String representing the symbol of the stock to be removed from the stocks map.
     */
    @Override
    public void removeEntry(String stockId) {
        stocks.remove(stockId);
    }

    /**
     * Method for checking if a stock can be found in the container.
     *
     * @param id String symbol representing the stock in the current StockContainer.
     * @return True if the symbol was found, false if not.
     */
    @Override
    public boolean hasEntry(String id) {
        return stocks.containsKey(id);
    }

    /**
     * Method for updating the market capitalization and price per share of a stock based on a processed limit order.
     * It uses the update method of the Stock object with the updated share price. The amountTraded parameter is not
     * used.
     *
     * @param limitOrder LimitOrder object with information about the processed order.
     */
    public void update(LimitOrder limitOrder) {
        Stock newStock = getStocks().get(limitOrder.getSymbol());
        newStock.update(limitOrder.getPrice());
        addEntry(newStock);
    }

    /**
     * Method for checking whether a stock container is empty or not.
     *
     * @return Boolean denoting the truth value corresponding to whether the container is empty or not.
     */
    @Override
    public boolean isEmpty() {
        return stocks.isEmpty();
    }

    /**
     * Method for retrieving the number of stocks in the container.
     *
     * @return Integer value representing the number of Stocks in the current StockContainer.
     */
    @Override
    public int getSize() {
        return stocks.size();
    }

    /**
     * Method for converting the current StockContainer object to a NetworkStockContainer object, which is used for
     * communication with traders.
     *
     * @return A NetworkStockContainer representation of the current StockContainer.
     */
    public NetworkStockContainer toNetworkStockContainer() {
        Map<String, NetworkStock> newContainer = new HashMap<>();
        for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
            newContainer.put(entry.getKey(), entry.getValue().toNetworkStock());
        }
        return new NetworkStockContainer(newContainer);
    }
}
