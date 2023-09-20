package nl.rug.aoop.orderresolver;

import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchange.StockExchange;

/**
 * SellLimitOrderResolver is a class containing functionality for resolving a sell limit order by matching it to
 * existing buy limit orders.
 */
public class SellLimitOrderResolver extends OrderResolver {

    /**
     * Constructor, sets the field variable stockExchange of superclass OrderResolver to a passed StockExchange object.
     *
     * @param stockExchange StockExchange object containing existing orders.
     */
    public SellLimitOrderResolver(StockExchange stockExchange) {
        super(stockExchange);
    }

    /**
     * Method for finding a matching buy order for a sell order. It first checks if the symbol of the stock to be sold
     * already exists. If so, it looks for the buy order with the highest maximum buy price over the sell order's
     * minimum sell price, and marks it as the matched order.
     *
     * @param order The sell LimitOrder to be matched against an existing buy order.
     * @return The matched buy LimitOrder, or null if none were found to match.
     */
    @Override
    public LimitOrder matchOrder(LimitOrder order) {
        if (!stockExchange.getBuyOrderMap().containsKey(order.getSymbol())) {
            return null;
        }
        LimitOrder bestMatch = new LimitOrder("", 0, 0, "", LimitOrder.Type.BUY);
        for (LimitOrder buyOrder : stockExchange.getBuyOrderMap().get(order.getSymbol())) {
            if (buyOrder.getPrice() >= order.getPrice() && bestMatch.getPrice() < buyOrder.getPrice() &&
                    !bestMatch.getTraderId().equals(order.getTraderId())) {
                bestMatch = buyOrder;
            }
        }
        if (bestMatch.getPrice() == 0) {
            return null;
        }
        return bestMatch;
    }
}
