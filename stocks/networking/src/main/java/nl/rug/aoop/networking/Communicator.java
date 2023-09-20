package nl.rug.aoop.networking;

/**
 * Interface made for classes which communicate over a network, implemented by ClientHandler and Client.
 */
public interface Communicator {
    /**
     * Terminates the running process and clean up.
     */
    void terminate();

    /**
     * Sends a message over the Network.
     *
     * @param message the message to be sent.
     */
    void sendMessage(String message);
}
