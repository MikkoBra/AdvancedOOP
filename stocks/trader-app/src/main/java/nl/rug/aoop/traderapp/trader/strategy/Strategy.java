package nl.rug.aoop.traderapp.trader.strategy;

import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;

import java.util.Map;

/**
 * Interface used for creating strategies which can be implemented by a Trader.
 */
public interface Strategy {

    /**
     * Method used for creating order based on the strategy.
     *
     * @param id             String id of the trader making the order.
     * @param funds          Double representing the funds available to the trader.
     * @param portfolio      Map containing String stock symbols and integer amounts representing the trader's
     *                       owned stocks.
     * @param stockContainer NetworkStockContainer object containing info on the Stock Market.
     * @return NetworkOrder object containing information about the created order.
     */
    NetworkOrder execute(String id, double funds, Map<String, Integer> portfolio, NetworkStockContainer stockContainer);
}
