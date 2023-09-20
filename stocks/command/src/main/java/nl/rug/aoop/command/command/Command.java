package nl.rug.aoop.command.command;

import java.io.IOException;
import java.util.Map;

/**
 * Command is an interface outlining the intended functionality of a Command.
 */
public interface Command {

    /**
     * Method that, once implemented, should execute the command it belongs to.
     *
     * @param params Map containing a String representation of a method parameter as a key, and the actual value of the
     *               intended parameter as a value.
     */
    void execute(Map<String, Object> params) throws IOException;
}
