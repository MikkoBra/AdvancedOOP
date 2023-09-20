package nl.rug.aoop.traderapp.stockexchange;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * LocalStockExchange is a class functioning as a local representation of a stock exchange, owned by a trader.
 */
@Slf4j
public class LocalStockExchange {
    @Getter
    private final Map<String, Integer> awaitingSellOrders;
    @Getter
    private NetworkStockContainer stockContainer;
    @Getter
    private NetworkTraderInfo traderInfo;
    @Getter
    private double withHeldFunds;

    /**
     * Constructor, sets field variables to corresponding passed parameter values.
     *
     * @param traderInfo     NetworkTraderInfo object to read trader information from.
     * @param stockContainer NetworkStockContainer object to read information about stocks from.
     */
    public LocalStockExchange(NetworkTraderInfo traderInfo, NetworkStockContainer stockContainer) {
        this.traderInfo = traderInfo;
        this.stockContainer = stockContainer;
        this.awaitingSellOrders = new HashMap<>();
        this.withHeldFunds = 0;
    }

    /**
     * Method for updating the info of a trader with info received from the stock app. It checks which sent sell orders
     * have been resolved, and updates the LocalStockExchange's information accordingly.
     *
     * @param receivedTraderInfo NetworkTraderInfo object containing information about the owner of
     *                           the LocalStockExchange.
     */
    public void updateTraderInfo(NetworkTraderInfo receivedTraderInfo) {
        receivedTraderInfo.getTransactionHistory().forEach((id, transaction) -> {
            if (!traderInfo.getTransactionHistory().containsKey(id)) {
                switch(transaction.getType()) {
                    case "SELL" -> updateAwaitingSellOrders(transaction.getSymbol(), -transaction.getAmount());
                    case "BUY" -> updateWithHeldFunds(transaction.getAmount(), -transaction.getPrice());
                }
            }
        });
        traderInfo = receivedTraderInfo;
    }

    /**
     * Method for updating field variable stockContainer with a passed stock container.
     *
     * @param receivedStockContainer NetworkStockContainer object containing the new information.
     */
    public void updateStockContainer(NetworkStockContainer receivedStockContainer) {
        stockContainer = receivedStockContainer;
    }

    /**
     * Method for updating the amount of active sell orders per owned stock.
     *
     * @param orderSymbol String symbol of the stock to update.
     * @param orderAmount Integer amount of new active sell orders.
     */
    public void updateAwaitingSellOrders(String orderSymbol, int orderAmount) {
        awaitingSellOrders.compute(orderSymbol, (symbol, amount) ->
                amount == null ? orderAmount : amount + orderAmount);
    }

    /**
     * Method for updating the amount of funds withheld because of an active unresolved sell order.
     *
     * @param orderAmount Integer amount of shares in the new sell order.
     * @param orderPrice  Double representing the sale price of one share in the sell order.
     */
    public void updateWithHeldFunds(int orderAmount, double orderPrice) {
        withHeldFunds += orderAmount * orderPrice;
    }

    /**
     * Getter method for the funds available to a trader, which equals their total funds minus funds spent on sent
     * orders.
     *
     * @return Double value representing the amount of funds available to a trader for stock trading.
     */
    public double getFunds() {
        return traderInfo.getFunds() - withHeldFunds;
    }

    /**
     * Getter method for the stocks available for sale to a trader, which equals their total shares minus the ones
     * already involved in an unprocessed sell order.
     *
     * @return Map of String keys and Integer values representing the amount of shares available to a trader per stock.
     */
    public Map<String, Integer> getPortfolio() {
        Map<String, Integer> correctedPortfolio = new HashMap<>();
        traderInfo.getStockPortfolio().forEach((symbol, amount) -> {
            int newAmount = amount - awaitingSellOrders.getOrDefault(symbol, 0);
            if (newAmount < 0) {
                log.error("Corrected portfolio amount reached below zero.");
            }
            correctedPortfolio.put(symbol, newAmount);
        });
        return correctedPortfolio;
    }

    /**
     * Method for checking whether the current LocalStockExchange has been initialized through an update from the
     * stock app.
     *
     * @return Boolean, true if traderInfo and stockContainer have been initialized, otherwise false.
     */
    public boolean hasReceivedInfo() {
        return traderInfo != null && stockContainer != null;
    }
}
