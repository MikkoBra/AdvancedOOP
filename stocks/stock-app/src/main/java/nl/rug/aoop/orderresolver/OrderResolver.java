package nl.rug.aoop.orderresolver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchange.StockExchange;

import static java.lang.Math.min;

/**
 * OrderResolver is an interface outlining the functionality of an object that processes orders sent to a stock
 * exchange.
 */
@Slf4j
public abstract class OrderResolver {

    /**
     * stockExchange is a field variable that is initialized and augmented by the subclasses of OrderResolver. It
     * contains unresolved orders.
     */
    @Getter
    protected StockExchange stockExchange;

    /**
     * Constructor, sets the field variable stockExchange to a passed StockExchange object.
     *
     * @param stockExchange StockExchange object containing existing orders.
     */
    public OrderResolver(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    /**
     * Method for resolving a buy order. It matches a buy order to a sell order that passes the prerequisites, and
     * trades the maximum amount of shares it can trade. If there are shares left in the buy order after a transaction,
     * a new sell order is searched for. This continues until all shares in the buy order are sold, or all matching
     * sell orders have been processed, in which case the buy order is stored in the stock exchange.
     *
     * @param order LimitOrder of type BUY, the order to be resolved.
     */
    public void resolveOrder(LimitOrder order) {
        while (order.getAmount() > 0) {
            LimitOrder bestMatch = matchOrder(order);
            if (bestMatch == null) {
                stockExchange.addOrder(order);
                break;
            }
            int amount = min(order.getAmount(), bestMatch.getAmount());
            stockExchange.getStockContainer().update(bestMatch);
            stockExchange.getTraderContainer().update(order, amount, bestMatch.getPrice());
            stockExchange.getTraderContainer().update(bestMatch, amount, bestMatch.getPrice());
            updateOrder(order, bestMatch, amount);
        }
    }

    /**
     * Method for updating the buy order and a matched sell order to signal a transaction.
     *
     * @param order        The buy LimitOrder involved in the transaction.
     * @param matchedOrder The sell LimitOrder involved in the transaction.
     * @param amountTraded Integer value representing the amount of shares traded in the transaction.
     */
    public void updateOrder(LimitOrder order, LimitOrder matchedOrder, int amountTraded) {
        stockExchange.updateOrder(matchedOrder, amountTraded);
        order.updateAmount(amountTraded);
    }

    /**
     * Method for matching an order with the best order currently in the stock exchange according to criteria.
     *
     * @param order The LimitOrder to be matched.
     * @return The matched LimitOrder.
     */
    public abstract LimitOrder matchOrder(LimitOrder order);
}
