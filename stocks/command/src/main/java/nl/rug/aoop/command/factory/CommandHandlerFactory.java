package nl.rug.aoop.command.factory;

import nl.rug.aoop.command.commandhandler.CommandHandler;

/**
 * Interface used for creating CommandHandlers with the required available commands.
 */
public interface CommandHandlerFactory {

    /**
     * Creates a CommandHandler with the correct commands registered.
     *
     * @return CommandHandler with commands registered.
     */
    CommandHandler create();
}
