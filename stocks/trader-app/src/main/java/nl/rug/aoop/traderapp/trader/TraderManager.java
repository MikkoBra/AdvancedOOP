package nl.rug.aoop.traderapp.trader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.mqproducer.NetworkProducer;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.client.ClientFactory;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
import nl.rug.aoop.traderapp.commands.TraderCommandHandlerFactory;
import nl.rug.aoop.traderapp.registrator.Registrator;
import nl.rug.aoop.traderapp.stockexchange.LocalStockExchange;
import nl.rug.aoop.traderapp.stockinteractor.StockInteractor;
import nl.rug.aoop.traderapp.trader.strategy.RandomStrategy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * Initializes a number of traders and runs them on separate threads.
 */
@Slf4j
public class TraderManager {
    @Getter
    private final InetSocketAddress address;
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutorService;
    @Getter
    private final List<Trader> traderList;
    @Getter
    private final List<Client> clientList;
    private final int numTraders;

    /**
     * Basic Constructor.
     *
     * @param address    InetSocketAddress used for communicating over the Network.
     * @param numTraders number of Traders to be initialized.
     */
    public TraderManager(InetSocketAddress address, int numTraders) {
        executorService = Executors.newCachedThreadPool();
        scheduledExecutorService = Executors.newScheduledThreadPool(numTraders);
        traderList = new ArrayList<>();
        clientList = new ArrayList<>();
        this.address = address;
        this.numTraders = numTraders;
    }

    /**
     * Sets up {@link TraderManager#numTraders} amount of traders. The Traders are connected to the Network through a
     * Client and are ran on separate threads.
     */
    public void setUpTraders() {
        for (int i = 1; i <= numTraders; i++) {
            String traderId = "bot" + i;
            NetworkTraderInfo traderInfo = new NetworkTraderInfo();
            NetworkStockContainer stockContainer = new NetworkStockContainer();
            LocalStockExchange localStockExchange = new LocalStockExchange(traderInfo, stockContainer);
            TraderCommandHandlerFactory factory = new TraderCommandHandlerFactory(localStockExchange);
            Client client;
            try {
                client = ClientFactory.createClientWithFactory(address, factory);
            } catch (IOException e) {
                log.error("Something went wrong when creating a Client for trader: " + traderId, e);
                continue;
            }
            clientList.add(client);
            executorService.submit(client);

            await().atMost(1, TimeUnit.SECONDS).until(() -> client.isRunning() && client.isConnected());

            NetworkProducer networkProducer = new NetworkProducer(client);
            Registrator registrator = new Registrator(client);
            StockInteractor stockInteractor = new StockInteractor(localStockExchange, networkProducer, registrator);
            Trader trader = new Trader(traderId, new RandomStrategy(), stockInteractor);
            traderList.add(trader);
            executorService.submit(trader);
        }
    }

    /**
     * Shuts down all Traders and their Clients and threads.
     */
    public void shutdownTraders() {
        clientList.forEach(Client::terminate);
        clientList.clear();
        traderList.forEach(Trader::terminate);
        traderList.clear();

        shutDownThreadPool(executorService);
        shutDownThreadPool(scheduledExecutorService);
    }

    /**
     * Method for shutting down all running threads at once.
     *
     * @param executorService ExecutorService object holding the running threads.
     */
    private void shutDownThreadPool(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                log.warn("Not all tasks have been finished before terminating");
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting");
        } finally {
            executorService.shutdownNow();
        }
    }
}
