package nl.rug.aoop.messagequeue.message;

import java.util.Date;

/**
 * QueueMessage is an immutable Message object that is used for communicating through a MessageQueue. It implements
 * Comparable such that two QueueMessages can be compared based on their timestamps.
 */
public class QueueMessage implements Comparable<QueueMessage> {

    private final Date timestamp;
    private final String header;
    private final String body;

    /**
     * Constructor, parameters are passed for the header and body of the message, and a given Date for the
     * timestamp.
     *
     * @param header    a string used to identify messages, it is not necessarily unique.
     * @param body      a string that contains content of the message.
     * @param timestamp Time of creation of original object.
     */
    public QueueMessage(String header, String body, Date timestamp) {
        this.header = header;
        this.body = body;
        this.timestamp = timestamp;
    }

    /**
     * Constructor, the field timestamp is set to the time when the QueueMessage is created.
     *
     * @param header a string used to identify messages, it is not necessarily unique.
     * @param body   a string that contains content of the message.
     */
    public QueueMessage(String header, String body) {
        this(header, body, new java.util.Date());
    }

    /**
     * Default constructor, sets header and body to empty strings.
     */
    public QueueMessage() {
        this("", "");
    }

    /**
     * Simple getter method for the message's header.
     *
     * @return A string containing the header of the message.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Simple getter method for the message's body.
     *
     * @return A string containing the body of the message.
     */
    public String getBody() {
        return body;
    }

    /**
     * Simple getter method for the message's timestamp.
     *
     * @return A Date object containing the time at which the message was created.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Method used for comparing QueueMessages based on their timestamps. Uses the compareTo() method of Date.
     *
     * @param message the object to be compared.
     * @return Negative integer if the parameter message has a later timestamp, positive if earlier.
     */
    @Override
    public int compareTo(QueueMessage message) {
        return timestamp.compareTo(message.getTimestamp());
    }

}
