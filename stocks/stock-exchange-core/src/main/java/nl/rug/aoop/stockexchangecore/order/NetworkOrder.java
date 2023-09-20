package nl.rug.aoop.stockexchangecore.order;

import lombok.Getter;

/**
 * Contains information about an order created by a client, is converted to a string and sent over the Network.
 */
@Getter
public class NetworkOrder {
    private final String symbol;
    private final int amount;
    private final double price;
    private final String traderId;
    private final String type;

    /**
     * Basic Constructor.
     *
     * @param symbol   symbol of company to buy shares off.
     * @param amount   amount of shares to be bought.
     * @param price    price at which the Trader wants to place the order.
     * @param traderId ID of trader that created the order.
     * @param type     type of order.
     */
    public NetworkOrder(String symbol, int amount, double price, String traderId, String type) {
        this.symbol = symbol;
        this.amount = amount;
        this.price = price;
        this.traderId = traderId;
        this.type = type;
    }

    /**
     * Enum containing constant values representing the type of a LimitOrder.
     */
    public enum Type {
        BUY,
        SELL,
        UNDEFINED
    }
}
