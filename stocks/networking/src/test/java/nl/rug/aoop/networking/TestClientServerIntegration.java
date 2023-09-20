package nl.rug.aoop.networking;

import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.messagehandler.MessageHandler;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClientServerIntegration {

    @Test
    public void TestClientServerSingleConnection() throws IOException {
        MessageHandler serverMockHandler = Mockito.mock(MessageHandler.class);
        Server server = new Server(0, serverMockHandler);
        new Thread(server).start();
        await().atMost(1, TimeUnit.SECONDS).until(server::isRunning);
        int numClientsBefore = server.getNumClients();

        InetSocketAddress address = new InetSocketAddress("localhost", server.getPort());
        MessageHandler clientMockHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, clientMockHandler);
        new Thread(client).start();
        await().atMost(1, TimeUnit.SECONDS).until(() -> client.isRunning() && client.isConnected());
        assertEquals(server.getNumClients(), numClientsBefore + 1);

        String testMessage = "Message from client";
        client.sendMessage(testMessage);
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() ->
                Mockito.verify(serverMockHandler).handleMessage(testMessage, server.getClientHandlerList().get(0)));
    }
}
