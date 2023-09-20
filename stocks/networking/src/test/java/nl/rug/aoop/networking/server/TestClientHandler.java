package nl.rug.aoop.networking.server;

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
public class TestClientHandler {
    private static final int TIMEOUT = 4000;
    private MessageHandler mockHandler;
    private int port;
    private boolean serverRunning;
    private InetSocketAddress address;
    private Socket serverSideSocket = null;

    private void startServer() {
        serverRunning = false;
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(0);
                port = serverSocket.getLocalPort();
                serverRunning = true;

                serverSideSocket = serverSocket.accept();
                log.info("Set up barebone testing server.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        await().atMost(2, TimeUnit.SECONDS).until(() -> serverRunning);
    }

    @BeforeEach
    public void setUp() {
        startServer();
        address = new InetSocketAddress("localhost", port);
        mockHandler = Mockito.mock(MessageHandler.class);
    }

    @Test
    public void testConstructor() throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(address);
            ClientHandler clientHandler = new ClientHandler(socket, mockHandler);
            assertEquals(clientHandler.getSocket(), socket);
            assertEquals(clientHandler.getMessageHandler(), mockHandler);
        }
    }

    @Test
    public void testRun() throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(address);
            await().atMost(2, TimeUnit.SECONDS).until(() -> serverSideSocket != null);
            ClientHandler clientHandler = new ClientHandler(serverSideSocket, mockHandler);
            new Thread(clientHandler).start();
            await().atMost(2, TimeUnit.SECONDS).until(clientHandler::isRunning);
            assertTrue(clientHandler.isRunning());

            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = "test message";
            out.println(message);
            out.flush();
            await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> Mockito.verify(mockHandler).handleMessage(message, clientHandler));
        }
    }

    @Test
    public void testTerminate() throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(address);
            ClientHandler clientHandler = new ClientHandler(socket, mockHandler);
            new Thread(clientHandler).start();
            await().atMost(2, TimeUnit.SECONDS).until(clientHandler::isRunning);

            clientHandler.terminate();
            await().atMost(2, TimeUnit.SECONDS).until(() -> !clientHandler.isRunning());
            assertFalse(clientHandler.isRunning());
            assertTrue(socket.isClosed());
        }
    }


}
