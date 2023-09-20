package nl.rug.aoop;

import nl.rug.aoop.traderapp.trader.TraderManager;

import java.net.InetSocketAddress;

/**
 * Main class for the Trader application. Uses TraderManager class to start a number of traders.
 */
public class Main {
    /**
     * Main method. Uses TraderManager class to start a number of traders.
     *
     * @param args default args.
     */
    public static void main(String[] args) {
        String portString = System.getenv("STOCK_EXCHANGE_PORT");
        String host = System.getenv("STOCK_EXCHANGE_HOST");
        if (host == null) {
            host = "localhost";
        }
        int port = portString == null ? 60606 : Integer.parseInt(portString);
        InetSocketAddress address = new InetSocketAddress(host, port);
        TraderManager traderManager = new TraderManager(address, 9);
        traderManager.setUpTraders();
    }
}