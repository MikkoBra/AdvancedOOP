package nl.rug.aoop.networking.messagehandler;

import lombok.Getter;
import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.networking.Communicator;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * ServerMessageHandler is a class that defines functionality for handling an incoming message on the Server side of
 * a network.
 */
public class ServerMessageHandler implements MessageHandler {

    @Getter
    private final CommandHandler commandHandler;

    /**
     * Constructor, sets CommandHandler field commandHandler to a passed CommandHandler object.
     *
     * @param commandHandler The CommandHandler that should handle the execution of a command on an incoming
     *                       QueueMessage.
     */
    public ServerMessageHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Method for converting a JSON string to a NetworkMessage object, extracting the string representation of a command
     * and QueueMessage from it, and passing them to commandHandler.
     *
     * @param message      JSON String representation of a NetworkMessage object.
     * @param communicator Reference to communicator which can be used to send a message back.
     */
    @Override
    public void handleMessage(String message, Communicator communicator) {
        NetworkMessage networkMessage = Converter.fromJson(message, NetworkMessage.class);
        String command = networkMessage.getHeader();

        Map<String, Object> params = new HashMap<>();
        params.put("header", networkMessage.getHeader());
        params.put("body", networkMessage.getBody());
        params.put("communicator", communicator);
        commandHandler.executeCommand(command, params);
    }
}
