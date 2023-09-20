package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.commands.ConnectionCommandHandlerFactory;
import nl.rug.aoop.commands.StockCommandHandlerFactory;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.messagequeue.NetworkOrderedQueue;
import nl.rug.aoop.networking.messagehandler.MessageHandler;
import nl.rug.aoop.networking.messagehandler.ServerMessageHandler;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.stockexchange.ConnectionManager;
import nl.rug.aoop.stockexchange.StockConsumer;
import nl.rug.aoop.stockexchange.StockExchange;
import nl.rug.aoop.stockexchange.StockExchangeFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Stock App is a stock exchange application which can be initialized either manually or through a .yaml file. It
 * keeps track of stocks, and allows traders to connect and interact with the stock exchange by creating orders.
 */
@Slf4j
public class Main {

    /**
     * Main method, initializes and starts up the stock application.
     *
     * @param args An array of command line arguments for the application if required.
     */
    public static void main(String[] args) {
        StockExchange stockExchange;
        try {
            stockExchange = new StockExchangeFactory().initialize("yaml");
        } catch (IOException e) {
            log.error("Something went wrong in initializing the stock exchange from .yaml");
            throw new RuntimeException(e);
        }

        NetworkOrderedQueue queue = new NetworkOrderedQueue();
        CommandHandler orderHandler = new StockCommandHandlerFactory(stockExchange).create();
        StockConsumer queueManager = new StockConsumer(stockExchange, orderHandler, queue);

        ConnectionManager connectionManager = new ConnectionManager(stockExchange);

        CommandHandler networkCommandHandler = new ConnectionCommandHandlerFactory(connectionManager,
                queue).create();
        MessageHandler messageHandler = new ServerMessageHandler(networkCommandHandler);
        String portString = System.getenv("STOCK_EXCHANGE_PORT");
        int port = portString == null ? 60606 : Integer.parseInt(portString);
        Server server = new Server(port, messageHandler);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(server);
        executorService.submit(connectionManager);

        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockExchange);
    }
}