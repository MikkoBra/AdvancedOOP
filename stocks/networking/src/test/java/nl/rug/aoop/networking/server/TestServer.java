package nl.rug.aoop.networking.server;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messagehandler.MessageHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestServer {

    private final int randomPort = 0;
    private final int illegalPort = -2;
    private MessageHandler mockHandler;
    private Server server;

    @BeforeEach
    public void setUp() {
        mockHandler = Mockito.mock(MessageHandler.class);
    }

    @AfterEach
    public void close() {
        if (this.server != null) {
            server.terminate();
        }
    }

    @Test
    public void testConstructor() throws IOException {
        server = new Server(randomPort, mockHandler);
        assertEquals(server.getMessageHandler(), mockHandler);
    }

    @Test
    public void testConstructorIllegalPort() {
        assertThrows(IllegalArgumentException.class, () -> new Server(illegalPort, mockHandler));
    }

    @Test
    public void testRunWithSingleConnection() throws IOException {
        server = new Server(randomPort, mockHandler);
        new Thread(server).start();
        await().atMost(3, TimeUnit.SECONDS).until(server::isRunning);
        assertTrue(server.isRunning());

        int startingNumClients = server.getNumClients();
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", server.getPort()));
            await().atMost(2, TimeUnit.SECONDS).until(
                    () -> server.getNumClients() == startingNumClients + 1);
            assertEquals(server.getNumClients(), startingNumClients + 1);
        }
    }

    @Test
    public void testTerminate() throws IOException {
        server = new Server(randomPort, mockHandler);
        new Thread(server).start();
        await().atMost(2, TimeUnit.SECONDS).until(server::isRunning);
        assertTrue(server.isRunning());
        server.terminate();
        await().atMost(2, TimeUnit.SECONDS).until(() -> !server.isRunning());
        assertFalse(server.isRunning());
    }

}
