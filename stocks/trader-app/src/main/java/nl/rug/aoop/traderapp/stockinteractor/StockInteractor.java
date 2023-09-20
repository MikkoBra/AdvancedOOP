package nl.rug.aoop.traderapp.stockinteractor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.mqproducer.NetworkProducer;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.traderapp.registrator.Registrator;
import nl.rug.aoop.traderapp.stockexchange.LocalStockExchange;

import java.io.IOException;
import java.util.Map;

/**
 * Used for interacting with the Stock Application on the side of the Server.
 */
@Slf4j
public class StockInteractor {
    @Getter
    private final LocalStockExchange localStockExchange;
    @Getter
    private final NetworkProducer networkProducer;
    @Getter
    private final Registrator registrator;

    /**
     * Basic constructor.
     *
     * @param localStockExchange LocalStockExchange object representing the most up to date knowledge a trader has on
     *                           the stock exchange.
     * @param networkProducer    NetworkProducer object which is able to send messages over a network.
     * @param registrator        Registrator object which handles the registration of the trader to the stock exchange.
     */
    public StockInteractor(LocalStockExchange localStockExchange, NetworkProducer networkProducer,
                           Registrator registrator) {
        this.localStockExchange = localStockExchange;
        this.networkProducer = networkProducer;
        this.registrator = registrator;
    }

    /**
     * Sends an order.
     *
     * @param order order to be sent.
     * @throws IOException when the message is null.
     */
    public void sendOrder(NetworkOrder order) throws IOException {
        if (order == null) {
            throw new IOException();
        }
        if (order.getType().equals("sell")) {
            localStockExchange.updateAwaitingSellOrders(order.getSymbol(), order.getAmount());
        }
        if (order.getType().equals("buy")) {
            localStockExchange.updateWithHeldFunds(order.getAmount(), order.getPrice());
        }
        QueueMessage message = new QueueMessage(order.getType(), Converter.toJson(order));
        networkProducer.put(message);
    }

    /**
     * Registers Trader.
     *
     * @param id id of Trader to be registered.
     */
    public void register(String id) {
        registrator.sendRegistration(new NetworkMessage("register", id));
    }

    /**
     * Deregisters an agent.
     *
     * @param id id of Trader to be deregistered.
     */
    public void unregister(String id) {
        registrator.sendRegistration(new NetworkMessage("unregister", id));
    }

    /**
     * Getter used for checking if a stock interactor is registered by checking if information has been received from
     * the stock app side.
     *
     * @return Boolean, true if the StockInteractor has been registered, false if not.
     */
    public boolean isRegistered() {
        return localStockExchange.hasReceivedInfo();
    }

    /**
     * Getter method for the funds available to a trader.
     *
     * @return Double representing the funds available to the owner of the current StockInteractor.
     */
    public double getFunds() {
        return localStockExchange.getFunds();
    }

    /**
     * Method for retrieving the stock portfolio of a trader.
     *
     * @return Map containing String symbols of stocks as keys, and the amount of shares per stock as an Integer value.
     */
    public Map<String, Integer> getPortfolio() {
        return localStockExchange.getPortfolio();
    }

    /**
     * Method for retrieving the most current information about all stocks in the stock exchange.
     *
     * @return NetworkStockContainer object containing information about all stocks in the stock exchange.
     */
    public NetworkStockContainer getStockContainer() {
        return localStockExchange.getStockContainer();
    }
}
