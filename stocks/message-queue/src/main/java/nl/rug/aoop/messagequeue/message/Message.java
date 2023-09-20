package nl.rug.aoop.messagequeue.message;

/**
 * Message is an interface outlining the functionality of a Message object.
 */
public interface Message {

    /**
     * Method for converting a Message object to its Json representation.
     *
     * @return A String in Json format containing the representation of the Message.
     */
    String toString();

    /**
     * Method for converting a Json String representation of a Message into a Message object.
     *
     * @param Json Json String representation of a Message.
     * @return The Message read from the Json String.
     */
    Message fromString(String Json);
}
