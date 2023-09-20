package nl.rug.aoop.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.networking.Communicator;
import nl.rug.aoop.stockexchange.ConnectionManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RegisterCommand is a class containing functionality for executing the registration of a trader to a stock exchange.
 */
@Slf4j
public class RegisterCommand implements Command {

    @Getter
    private final ConnectionManager connectionManager;

    /**
     * Constructor, sets field connectionManager to a passed ConnectionManager object containing the implementation of
     * registering a trader.
     *
     * @param connectionManager ConnectionManager object the trader should be registered in.
     */
    public RegisterCommand(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Method for executing the registration. It takes a trader id and a communicator object from the passed parameter
     * map, and passes them to the registration method of the Command's ConnectionManager.
     *
     * @param params Map containing a String representation of a method parameter as a key, and the actual value of the
     *               intended parameter as a value.
     * @throws IOException When the parameter map was not correctly initialized or contains the wrong information.
     */
    @Override
    public void execute(Map<String, Object> params) throws IOException {
        if (!params.containsKey("body")) {
            throw new IOException("RegisterCommand was called without body parameter key.");
        } else if (!params.containsKey("communicator")) {
            throw new IOException("RegisterCommand was called without communicator parameter key.");
        }
        String traderId;
        Communicator communicator;
        try {
            traderId = (String) params.get("body");
            communicator = (Communicator) params.get("communicator");
        } catch (ClassCastException e) {
            log.error("A parameter object was not castable.");
            throw new IOException();
        }
        connectionManager.connectTrader(traderId, communicator);
        if (!connectionManager.isRunning()) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(connectionManager);
        }
    }
}
