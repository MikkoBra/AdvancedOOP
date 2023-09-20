package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messagehandler.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestClient {
    private int port;
    private boolean serverRunning;
    private PrintWriter serverOut;
    private BufferedReader serverIn;
    private InetSocketAddress address;
    private MessageHandler mockHandler;

    private void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(0);
                port = serverSocket.getLocalPort();

                serverRunning = true;

                Socket clientSocket = serverSocket.accept();
                serverOut = new PrintWriter(clientSocket.getOutputStream());
                serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                log.info("Set up barebone testing server.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
        await().atMost(4, TimeUnit.SECONDS).until(() -> serverRunning);
    }

    @BeforeEach
    public void setUp() {
        startServer();
        address = new InetSocketAddress("localhost", port);
        mockHandler = Mockito.mock(MessageHandler.class);
    }

    @Test
    public void testConstructorWithServer() throws IOException {
        Client client = new Client(address, mockHandler);
        assertTrue(client.isConnected());
    }

    @Test
    public void testRun() throws IOException {
        Client client = new Client(address, mockHandler);
        assertFalse(client.isRunning());
        new Thread(client).start();
        await().atMost(4, TimeUnit.SECONDS).until(client::isRunning);
        assertTrue(client.isRunning());

        String message = "test message";
        serverOut.println(message);
        serverOut.flush();
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() ->
                Mockito.verify(mockHandler).handleMessage(message, client));
    }

    @Test
    public void testTerminate() throws IOException {
        Client client = new Client(address, mockHandler);
        new Thread(client).start();
        await().atMost(3, TimeUnit.SECONDS).until(client::isRunning);
        assertTrue(client.isRunning());
        assertTrue(client.isConnected());
        client.terminate();
        assertFalse(client.isRunning());
        assertFalse(client.isConnected());
    }

    @Test
    public void testSendMessage() throws IOException {
        Client client = new Client(address, mockHandler);
        new Thread(client).start();
        await().atMost(1, TimeUnit.SECONDS).until(client::isConnected);
        String message = "test message";
        client.sendMessage(message);
        assertEquals(serverIn.readLine(), message);
    }
}
