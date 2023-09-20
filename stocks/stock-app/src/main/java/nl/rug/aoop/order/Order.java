package nl.rug.aoop.order;

import nl.rug.aoop.stockexchangecore.order.NetworkOrder;

/**
 * Order is an abstract class that outlines the functionality of an order in a stock exchange.
 */
public abstract class Order {

    /**
     * Method for setting the fields of the Order object according to passed information.
     *
     * @param params NetworkOrder containing information about fields of the Order object.
     */
    public abstract void setInformation(NetworkOrder params);
}
