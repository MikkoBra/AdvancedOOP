package nl.rug.aoop.networking.networkmessage;

/**
 * Class defining the functionality of a NetworkMessage, which is an implementation of Message used to send information
 * over a network.
 */
public class NetworkMessage {
    private final String header;
    private final String body;

    /**
     * Constructor, initializes String fields header and body with passed parameters for the intended command and
     * message.
     *
     * @param command String reference to the intended command registered in a CommandHandler.
     * @param message Json String representation of a QueueMessage object.
     */
    public NetworkMessage(String command, String message) {
        header = command;
        body = message;
    }

    /**
     * Default constructor, uses NetworkMessage(command, message) but initializes the header and body fields with
     * empty Strings.
     */
    public NetworkMessage() {
        this("", "");
    }

    /**
     * Getter for the header of the NetworkMessage.
     *
     * @return A String representing the header of the NetworkMessage.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Getter for the body of the NetworkMessage.
     *
     * @return A String representing the body of the NetworkMessage.
     */
    public String getBody() {
        return body;
    }
}