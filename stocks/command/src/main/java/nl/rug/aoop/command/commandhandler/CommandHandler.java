package nl.rug.aoop.command.commandhandler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CommandHandler describes the functionality of an object that contains commands, retrieves the right command to
 * execute based on input parameters and executes it.
 */
@Slf4j
public class CommandHandler {
    @Getter
    private final Map<String, Command> commandMap;

    /**
     * Constructor, initializes the Map field commandMap with an empty HashMap.
     */
    public CommandHandler() {
        commandMap = new HashMap<>();
    }

    /**
     * Method that retrieves the right command from commandMap based on what was queried, and executes it.
     *
     * @param command String representing the command to be retrieved from commandMap.
     * @param params  Map containing String representations of parameters to be passed to the command, and the actual
     *                value of said parameter.
     */
    public void executeCommand(String command, Map<String, Object> params) {
        if (commandMap.containsKey(command)) {
            try {
                commandMap.get(command).execute(params);
            } catch (IOException e) {
                log.error("Command could not be executed with the params" + params, e);
            }
        } else {
            log.error("Command: " + command + " not found.");
        }
    }

    /**
     * Method that registers a new command in commandMap, such that it can be retrieved for execution by executeCommand.
     *
     * @param name    String reference to the command that is to be registered.
     * @param command The command to be registered.
     */
    public void registerCommand(String name, Command command) {
        if (command != null) {
            commandMap.put(name, command);
        } else {
            log.error("Command was null, therefore not registered.");
        }
    }
}
