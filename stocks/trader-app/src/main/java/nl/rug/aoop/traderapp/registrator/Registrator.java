package nl.rug.aoop.traderapp.registrator;

import lombok.Getter;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;

/**
 * Sends messages that register or deregister Traders.
 */
public class Registrator {
    @Getter
    private final Client client;

    /**
     * Basic Constructor.
     *
     * @param client client used for sending the messages.
     */
    public Registrator(Client client) {
        this.client = client;
    }

    /**
     * Method used for sending the registration through the Client.
     *
     * @param message message containing info on registration.
     */
    public void sendRegistration(NetworkMessage message) {
        client.sendMessage(Converter.toJson(message));
    }
}
