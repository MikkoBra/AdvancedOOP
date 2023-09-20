package nl.rug.aoop.networking.messagehandler;

import nl.rug.aoop.networking.Communicator;

/**
 * The MessageHandler interface is used for handling messages sent over a network.
 */
public interface MessageHandler {
    /**
     * Handles message sent over a network.
     *
     * @param message      JSON String representation of a message object.
     * @param communicator Reference to communicator which can be used to send a message back.
     */
    void handleMessage(String message, Communicator communicator);
}
