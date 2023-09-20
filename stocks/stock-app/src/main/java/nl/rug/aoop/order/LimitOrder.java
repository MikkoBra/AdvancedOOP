package nl.rug.aoop.order;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;

/**
 * LimitOrder is a class defining the functionality of a limit order containing information about a desired stock
 * transaction.
 */
@Slf4j
public class LimitOrder extends Order {

    @Getter
    private String symbol;
    @Getter
    private int amount;
    @Getter
    private double price;
    @Getter
    private String traderId;
    @Getter
    private Type type;

    /**
     * Constructor, takes information about a stock transaction as parameters and sets field variables accordingly.
     *
     * @param symbol   String holding the symbol of the stock to be traded.
     * @param amount   Integer denoting the amount of shares to be traded.
     * @param price    Double denoting the intended minimum/maximum price per share of the transaction.
     * @param traderId String id representing the trader who requested the order.
     * @param type     Type of order that was issued, either BUY or SELL. Use UNDEFINED when not applicable.
     * @throws IllegalArgumentException If the entered order type was not recognized by the LimitOrder class.
     */
    public LimitOrder(String symbol, int amount, double price, String traderId, Type type) throws
            IllegalArgumentException {
        if (!type.equals(Type.BUY) && !type.equals(Type.SELL) && !type.equals(Type.UNDEFINED)) {
            throw new IllegalArgumentException("The given order type was not expected. Please enter Limitorder.BUY or" +
                    "Limitorder.SELL");
        }
        this.type = type;
        this.symbol = symbol;
        this.amount = amount;
        this.price = price;
        this.traderId = traderId;
    }

    /**
     * Default constructor, initializes the field variables with default values representing nothing.
     */
    public LimitOrder() {
        this("", 0, 0, "", Type.UNDEFINED);
    }

    /**
     * Method for setting the field variables of the current limit order to those of a passed NetworkOrder object.
     *
     * @param order NetworkOrder containing information about fields of the LimitOrder object.
     */
    @Override
    public void setInformation(NetworkOrder order) {
        symbol = order.getSymbol();
        amount = order.getAmount();
        price = order.getPrice();
        traderId = order.getTraderId();
        switch (order.getType()) {
            case "buy":
                type = Type.BUY;
                break;
            case "sell":
                type = Type.SELL;
                break;
            case "":
                type = Type.UNDEFINED;
        }
    }

    /**
     * Method for setting the amount left in an order after a transaction. If a higher value is passed than what was
     * left in the order, the amount is set to 0.
     *
     * @param amount Integer value for the amount to be deducted from the LimitOrder amount.
     */
    public void updateAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("updateAmount() was called with a negative value.");
        }
        if (amount <= this.amount) {
            this.amount -= amount;
        } else {
            this.amount = 0;
        }
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
