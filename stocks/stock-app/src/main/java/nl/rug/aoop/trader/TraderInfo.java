package nl.rug.aoop.trader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TraderInfo is a class containing functionality to store and manipulate information about a trader in a stock
 * exchange. Keeps track of the trader's transaction history.
 */
@Slf4j
public class TraderInfo implements TraderDataModel {
    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private double funds;
    @Getter
    private final Map<String, Integer> stockPortfolio;
    @Getter
    private final Map<Long, NetworkOrder> transactionHistory;
    private long personalOrderId;

    /**
     * Constructor, sets field variables to passed parameter values.
     *
     * @param id             String id representing the trader in the stock exchange.
     * @param name           String containing the actual name of the trader.
     * @param funds          Double value denoting the available funds of the trader.
     * @param stockPortfolio Map containing the amount of shares the trader has of each stock.
     */
    public TraderInfo(String id, String name, double funds, Map<String, Integer> stockPortfolio) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.stockPortfolio = stockPortfolio;
        this.personalOrderId = 0;
        transactionHistory = new HashMap<>();
    }

    /**
     * Method for updating the information of a trader based on a processed LimitOrder. The funds of the trader are
     * updated, as well as the amount of shares they have of the traded stock. The trader's transaction history is
     * updated through fundsSpent and sharesSold depending on whether the order is of type buy or sell.
     *
     * @param order        LimitOrder containing information about a processed transaction.
     * @param amountTraded Integer value representing the amount of shares traded in the transaction.
     * @param salePrice    Double value representing the price at which shares were sold in the transaction.
     */
    public void updateTrader(LimitOrder order, int amountTraded, double salePrice) {
        transactionHistory.put(personalOrderId, new NetworkOrder(order.getSymbol(), amountTraded, salePrice, id,
                order.getType().name()));
        personalOrderId += 1;
        if (order.getType().equals(LimitOrder.Type.BUY)) {
            log.info("Trader " + id + " bought " + amountTraded + ", lost " + amountTraded * salePrice);
            funds -= salePrice * amountTraded;
            stockPortfolio.compute(order.getSymbol(), (symbol, amount) ->
                    amount == null ? amountTraded : amount + amountTraded);
        } else if (order.getType().equals(LimitOrder.Type.SELL)) {
            log.info("Trader " + id + " sold " + amountTraded + ", made " + amountTraded * salePrice);
            funds += salePrice * amountTraded;
            int newAmount = stockPortfolio.get(order.getSymbol()) - amountTraded;
            if (newAmount == 0) {
                stockPortfolio.remove(order.getSymbol());
            } else {
                stockPortfolio.put(order.getSymbol(), newAmount);
            }
        }
    }

    /**
     * Method for converting the current TraderInfo object into a NetworkTraderInfo object.
     *
     * @return A NetworkTraderInfo representation of the current TraderInfo object with the same field values.
     */
    public NetworkTraderInfo toNetworkTraderInfo() {
        return new NetworkTraderInfo(id, name, funds, stockPortfolio, transactionHistory);
    }

    /**
     * Method for retrieving a list of all owned stocks of the current trader.
     *
     * @return List of Strings containing the symbols of owned stocks.
     */
    @Override
    public List<String> getOwnedStocks() {
        return new ArrayList<>(stockPortfolio.keySet());
    }

    @Override
    public int getNumberOfOwnedShares(String stockSymbol) {
        return stockPortfolio.get(stockSymbol);
    }
}
