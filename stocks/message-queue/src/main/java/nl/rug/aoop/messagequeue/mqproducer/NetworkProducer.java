package nl.rug.aoop.messagequeue.mqproducer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;

/**
 * Implementation of MQProducer implementation. This class Produces messages that will be sent through a network.
 */
@Slf4j
public class NetworkProducer implements MQProducer {
    @Getter
    private final Client client;

    /**
     * Constructor, creates a Networkproducer object with the client as a field.
     *
     * @param client client which will be used to send messages
     * @throws IllegalArgumentException when client is not running or connected.
     */
    public NetworkProducer(Client client) throws IllegalArgumentException {
        if (!client.isRunning() || !client.isConnected()) {
            log.error("Client passed to NetworkProducer is either not running or not connected");
            throw new IllegalArgumentException();
        }
        this.client = client;
    }

    /**
     * Converts the message to a Json string and sends it to a MessageQueue using the client.
     *
     * @param message the QueueMessage object to be put in the MessageQueue.
     */
    @Override
    public void put(QueueMessage message) {
        if (message == null) {
            log.error("QueueMessage object was null, could not be processed.");
            return;
        }
        client.sendMessage(Converter.toJson(new NetworkMessage("MqPut", Converter.toJson(message))));
    }
}
