package nl.rug.aoop.stockexchange;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.containers.TraderContainer;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.order.LimitOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StockExchange is a class containing functionality for keeping track of the relevant information of a stock
 * exchange, and updating said information by removing, changing, and adding entries.
 */
@Slf4j
public class StockExchange implements StockExchangeDataModel {

    @Getter
    private final StockContainer stockContainer;
    @Getter
    private final TraderContainer traderContainer;
    @Getter
    private final Map<String, List<LimitOrder>> buyOrderMap;
    @Getter
    private final Map<String, List<LimitOrder>> sellOrderMap;

    /**
     * Constructor, initializes the StockContainer and TraderContainer field variables with passed parameter variables.
     *
     * @param stockContainer  StockContainer object containing all stocks to be initially registered in the stock
     *                        exchange.
     * @param traderContainer TraderContainer object containing all traders to be initially registered in the stock
     *                        exchange.
     */
    public StockExchange(StockContainer stockContainer, TraderContainer traderContainer) {
        this.stockContainer = stockContainer;
        this.traderContainer = traderContainer;
        this.buyOrderMap = new HashMap<>();
        this.sellOrderMap = new HashMap<>();
    }

    /**
     * Method for finding the corresponding order map field for an order.
     *
     * @param order The order to be matched to an order map.
     * @return buyOrderMap field if the passed order is a buy LimitOrder, sellOrderMap otherwise.
     */
    public Map<String, List<LimitOrder>> matchMap(LimitOrder order) {
        Map<String, List<LimitOrder>> orderMap;
        if (order.getType().equals(LimitOrder.Type.BUY)) {
            orderMap = buyOrderMap;
        } else if (order.getType().equals(LimitOrder.Type.SELL)) {
            orderMap = sellOrderMap;
        } else {
            log.error("Order type was not recognized, could not retrieve map.");
            return null;
        }
        return orderMap;
    }

    /**
     * Method for adding a LimitOrder to its respective (Buy/Sell)OrderMap field based on its type. Since each entry
     * in the map is a list of orders for a certain stock, the LimitOrder is first added to a list, either the existing
     * one or a new one, before being added to the map. An order will not be added if it contains no shares
     * (amount <= 0).
     *
     * @param order LimitOrder object to be stored in the current StockExchange object.
     */
    public void addOrder(LimitOrder order) {
        if (order.getAmount() > 0) {
            Map<String, List<LimitOrder>> orderMap = matchMap(order);
            List<LimitOrder> orderList;
            if (orderMap.containsKey(order.getSymbol())) {
                orderList = orderMap.get(order.getSymbol());
            } else {
                orderList = new ArrayList<>();
            }
            orderList.add(order);
            orderMap.put(order.getSymbol(), orderList);
        }
    }

    /**
     * Method for removing a LimitOrder from its respective (Buy/Sell)OrderMap field based on its type. Since each entry
     * in the map is a list of orders for a certain stock, the LimitOrder is locally removed from the existing list,
     * after which the updated list is re-entered in the map if it still has any entries left.
     *
     * @param order LimitOrder object to be removed from the current StockExchange object.
     */
    public void removeOrder(LimitOrder order) throws IllegalArgumentException {
        Map<String, List<LimitOrder>> orderMap = matchMap(order);
        List<LimitOrder> orderList = orderMap.get(order.getSymbol());
        if (orderList == null) {
            log.error("Tried to remove order that doesn't exist in the stock exchange.");
            throw new IllegalArgumentException();
        }
        orderList.remove(order);
        if (orderList.size() == 0) {
            orderMap.remove(order.getSymbol());
            return;
        }
        orderMap.put(order.getSymbol(), orderList);
    }

    /**
     * Method for updating the amount left in an order after a transaction. The order is taken out of the order
     * map. If the order contains a higher amount of shares than the given amount parameter, the order is added to
     * the stock exchange with an updated value. If the amount parameter value is equal to or higher than
     * the actual order amount, the order remains removed permanently.
     *
     * @param order LimitOrder object representing the to be updated order.
     * @param amount Integer value representing the amount of already-traded shares from the order.
     */
    public void updateOrder(LimitOrder order, int amount) {
        try {
            removeOrder(order);
        } catch (IllegalArgumentException e) {
            log.error("To be updated order does not exist yet in the stock exchange.", e);
            return;
        }
        try {
            order.updateAmount(amount);
        } catch (IllegalArgumentException e) {
            log.error("Update amount was a negative value, should be positive.", e);
            return;
        }
        addOrder(order);
    }

    /**
     * Method for retrieving a stock from the stock exchange by its index in the StockContainer field object.
     *
     * @param index The index of the stock that should be accessed.
     * @return StockDataModel object representing the retrieved stock.
     */
    @Override
    public StockDataModel getStockByIndex(int index) {
        return new ArrayList<>(stockContainer.getStocks().values()).get(index);
    }

    /**
     * Method for retrieving the number of stocks in the stock exchange.
     *
     * @return Integer value representing the number of stocks in the StockContainer field object.
     */
    @Override
    public int getNumberOfStocks() {
        return stockContainer.getSize();
    }

    /**
     * Method for retrieving a trader from the stock exchange by its index in the TraderContainer field object.
     *
     * @param index The index of the trader that should be accessed.
     * @return TraderDataModel object representing the retrieved trader.
     */
    @Override
    public TraderDataModel getTraderByIndex(int index) {
        return new ArrayList<>(traderContainer.getTraders().values()).get(index);
    }

    /**
     * Method for retrieving the number of stocks in the stock exchange.
     *
     * @return Integer value representing the number of stocks in the StockContainer field object.
     */
    @Override
    public int getNumberOfTraders() {
        return traderContainer.getSize();
    }

}
