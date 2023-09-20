package nl.rug.aoop.messagequeue.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.messagequeue.MessageQueue;
import nl.rug.aoop.networking.converter.Converter;

import java.io.IOException;
import java.util.Map;

/**
 * MqPutCommand is a Command class containing the functionality to put a QueueMessage in a MessageQueue.
 */
@Slf4j
public class MqPutCommand implements Command {
    @Getter
    private final MessageQueue messageQueue;

    /**
     * Constructor, initializes MessageQueue field messageQueue with a passed MessageQueue parameter.
     *
     * @param messageQueue The producer that performs the putting of a message in a MessageQueue
     */
    public MqPutCommand(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Method for extracting a QueueMessage from a passed parameter map and enqueueing the message in messageQueue.
     *
     * @param params Map containing a String representation of the "message" parameter, and the corresponding
     *               QueueMessage value.
     */
    @Override
    public void execute(Map<String, Object> params) throws IOException {
        if (!params.containsKey("body")) {
            throw new IOException("MqPutCommand was executed without body");
        }
        QueueMessage message;
        try {
            String jsonMessage = (String) params.get("body");
            message = Converter.fromJson(jsonMessage, QueueMessage.class);
        } catch (ClassCastException e) {
            log.error("Parameter object was not castable to String object.");
            return;
        }
        messageQueue.enqueue(message);
    }
}
