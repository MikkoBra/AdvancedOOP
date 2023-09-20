package nl.rug.aoop.stockexchange;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.containers.TraderContainer;
import nl.rug.aoop.networking.Communicator;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
import nl.rug.aoop.trader.TraderInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * ConnectionManager is a class which facilitates communication between a StockExchange object and a trader over a
 * network.
 */
@Slf4j
public class ConnectionManager implements Runnable {

    @Getter
    private final StockExchange stockExchange;
    @Getter
    private final Map<String, Communicator> connectedTraders;
    @Getter
    private boolean running;

    /**
     * Constructor, initializes StockExchange field variable stockExchange with a passed stock exchange, and
     * creates a new empty HashMap to keep track of connected traders.
     *
     * @param stockExchange StockExchange object containing stock exchange information
     */
    public ConnectionManager(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
        connectedTraders = new HashMap<>();
    }

    /**
     * Method for adding a trader to the list of connected traders, along with its dedicated Communicator object.
     *
     * @param traderId     String id representing the connected trader.
     * @param communicator Communicator object used to send information to the connected trader.
     * @throws IllegalArgumentException When a trader that was not registered in the stock exchange attempted
     *                                  to connect.
     */
    public void connectTrader(String traderId, Communicator communicator) throws IllegalArgumentException {
        log.info("Connecting trader: " + traderId);
        if (!stockExchange.getTraderContainer().hasEntry(traderId)) {
            log.error("A trader that did not exist in the trader registry of the stock exchange attempted to connect.");
            throw new IllegalArgumentException();
        }
        connectedTraders.put(traderId, communicator);
    }

    /**
     * Method for removing a trader from the list of connected traders.
     *
     * @param traderId String id representing the connected trader to be removed.
     * @throws IllegalArgumentException When a trader was passed that was not registered as connected.
     */
    public void disconnectTrader(String traderId) throws IllegalArgumentException {
        if (connectedTraders.remove(traderId) == null) {
            log.error("Tried to disconnect a trader that was not in the list of connected traders.");
            throw new IllegalArgumentException();
        }
    }

    /**
     * Method for sending the information of a trader stored in the stock exchange to the trader themselves, along with
     * information about all stocks in the stock exchange.
     *
     * @param communicator Communicator object belonging to the trader.
     * @param traderInfo   The information about the trader stored in the stock exchange.
     */
    public void sendMessages(Communicator communicator, TraderInfo traderInfo) {
        NetworkTraderInfo networkTraderInfo = traderInfo.toNetworkTraderInfo();
        NetworkMessage message = new NetworkMessage("updateTraderInfo", Converter.toJson(networkTraderInfo));
        String jsonMessage = Converter.toJson(message);
        communicator.sendMessage(jsonMessage);

        NetworkStockContainer stockContainer = stockExchange.getStockContainer().toNetworkStockContainer();
        message = new NetworkMessage("updateStockContainer", Converter.toJson(stockContainer));
        jsonMessage = Converter.toJson(message);
        communicator.sendMessage(jsonMessage);
    }

    /**
     * Method for sending stock exchange information to each connected trader. For each connected trader, their own
     * information and the information about all stocks in the exchange are collected and converted to objects with
     * minimal functionality outside of holding the information. After the conversion, the information is sent to each
     * trader through their respective Communicator objects. If no more traders are connected, the ConnectionManager
     * terminates.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            TraderContainer traders = stockExchange.getTraderContainer();
            for (Map.Entry<String, Communicator> entry : connectedTraders.entrySet()) {
                sendMessages(entry.getValue(), traders.getInfo(entry.getKey()));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.info("Thread was interrupted while sleeping.");
                throw new RuntimeException(e);
            }
            if (connectedTraders.isEmpty()) {
                terminate();
            }
        }
    }

    /**
     * Method for terminating the run method by setting field variable running to false.
     */
    public void terminate() {
        running = false;
    }
}
