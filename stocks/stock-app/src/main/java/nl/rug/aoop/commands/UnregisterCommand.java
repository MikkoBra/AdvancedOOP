package nl.rug.aoop.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.stockexchange.ConnectionManager;

import java.io.IOException;
import java.util.Map;

/**
 * UnregisterCommand is a class containing functionality for executing the unregistering of a trader from a
 * stock exchange.
 */
@Slf4j
public class UnregisterCommand implements Command {

    @Getter
    private final ConnectionManager connectionManager;

    /**
     * Constructor, sets field connectionManager to a passed ConnectionManager object containing the implementation of
     * unregistering a trader.
     *
     * @param connectionManager ConnectionManager object the trader should be unregistered from.
     */
    public UnregisterCommand(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Method for executing the unregistration. It takes a trader id from the passed parameter map, and passes it to
     * the unregister method of the Command's ConnectionManager.
     *
     * @param params Map containing a String representation of a method parameter as a key, and the actual value of the
     *               intended parameter as a value.
     * @throws IOException When the parameter map was not correctly initialized or contains the wrong information.
     */
    @Override
    public void execute(Map<String, Object> params) throws IOException {
        if (!params.containsKey("body")) {
            throw new IOException("Unregister command was executed without traderId");
        }
        String traderId;
        try {
            traderId = (String) params.get("body");
        } catch (ClassCastException e) {
            log.error("Parameter object was not castable to String.");
            throw new IOException();
        }
        connectionManager.disconnectTrader(traderId);
    }
}
