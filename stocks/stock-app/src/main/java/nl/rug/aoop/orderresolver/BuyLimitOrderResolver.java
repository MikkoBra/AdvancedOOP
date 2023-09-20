package nl.rug.aoop.orderresolver;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchange.StockExchange;

/**
 * BuyLimitOrderResolver is a class containing functionality for resolving a buy limit order by matching it to existing
 * sell limit orders.
 */
@Slf4j
public class BuyLimitOrderResolver extends OrderResolver {

    /**
     * Constructor, sets the field variable stockExchange of superclass OrderResolver to a passed StockExchange object.
     *
     * @param stockExchange StockExchange object containing existing orders.
     */
    public BuyLimitOrderResolver(StockExchange stockExchange) {
        super(stockExchange);
    }

    /**
     * Method for finding a matching sell order for a buy order. It first checks if the symbol of the stock to be bought
     * already exists. If so, it looks for the sell order with the lowest minimum sell price under the buy order's
     * maximum buy price, and marks it as the matched order.
     *
     * @param order The buy LimitOrder to be matched against an existing sell order.
     * @return The matched sell LimitOrder, or null if none were found to match.
     */
    @Override
    public LimitOrder matchOrder(LimitOrder order) {
        if (!stockExchange.getSellOrderMap().containsKey(order.getSymbol())) {
            return null;
        }
        LimitOrder bestMatch = new LimitOrder("", 0, Integer.MAX_VALUE, "", LimitOrder.Type.SELL);
        for (LimitOrder sellOrder : stockExchange.getSellOrderMap().get(order.getSymbol())) {
            if (sellOrder.getPrice() <= order.getPrice() && bestMatch.getPrice() > sellOrder.getPrice() &&
                    !bestMatch.getTraderId().equals(order.getTraderId())) {
                bestMatch = sellOrder;
            }
        }
        if (bestMatch.getPrice() == Integer.MAX_VALUE) {
            return null;
        }
        return bestMatch;
    }
}
