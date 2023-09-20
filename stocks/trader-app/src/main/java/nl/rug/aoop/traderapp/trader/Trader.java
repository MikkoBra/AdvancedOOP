package nl.rug.aoop.traderapp.trader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.traderapp.stockinteractor.StockInteractor;
import nl.rug.aoop.traderapp.trader.strategy.Strategy;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * A Bot representing a user that buys or sells shares through the Stock market. Implements Runnable so that it can be
 * run on a thread.
 */
@Slf4j
public class Trader implements Runnable {
    @Getter
    private final String id;
    @Getter
    private final StockInteractor stockInteractor;
    @Getter
    private final Strategy strategy;
    @Getter
    private boolean running = false;

    /**
     * Basic Constructor.
     *
     * @param id              id of trader.
     * @param strategy        strategy.
     * @param stockInteractor StockInteractor object which manages interactions between the trader and the stock
     *                        exchange.
     */
    public Trader(String id, Strategy strategy, StockInteractor stockInteractor) {
        this.id = id;
        this.stockInteractor = stockInteractor;
        this.strategy = strategy;
    }

    /**
     * Method for continuously sending orders to a stock exchange over a network.
     */
    @Override
    public void run() {
        Random random = new Random();
        running = true;
        stockInteractor.register(id);
        await().atMost(1, TimeUnit.SECONDS).until(stockInteractor::isRegistered);
        while (running) {
            try {
                Thread.sleep(random.nextInt(1000, 4000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (stockInteractor.isRegistered()) {
                NetworkOrder order = strategy.execute(id, stockInteractor.getFunds(), stockInteractor.getPortfolio(),
                        stockInteractor.getStockContainer());
                if (order != null) {
                    try {
                        stockInteractor.sendOrder(order);
                    } catch (IOException e) {
                        log.error("Order was null", e);
                    }
                } else {
                    terminate();
                    break;
                }
            }
        }
    }

    /**
     * Registers a trader in the Stock Application.
     */
    public void register() {
        stockInteractor.register(id);
    }

    /**
     * Terminates the process of creating orders and unregisters the trader.
     */
    public void terminate() {
        running = false;
        stockInteractor.unregister(id);
    }
}
