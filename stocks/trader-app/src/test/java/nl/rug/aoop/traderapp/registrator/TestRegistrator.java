package nl.rug.aoop.traderapp.registrator;

import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class TestRegistrator {

    private Client client;
    private Registrator registrator;

    @BeforeEach
    public void setUp() {
        client = Mockito.mock(Client.class);
        registrator = new Registrator(client);
    }

    @Test
    public void testRegistratorConstructor() {
        assertEquals(client, registrator.getClient());
    }

    @Test
    public void testSendRegistration() {
        NetworkMessage mockMessage = Mockito.mock(NetworkMessage.class);
        String jsonMessage = "test";
        try (MockedStatic<Converter> mockStatic = mockStatic(Converter.class)) {
            mockStatic.when(() -> Converter.toJson(mockMessage))
                    .thenReturn(jsonMessage);
            registrator.sendRegistration(mockMessage);
            Mockito.verify(client).sendMessage(jsonMessage);
        }
    }
}
