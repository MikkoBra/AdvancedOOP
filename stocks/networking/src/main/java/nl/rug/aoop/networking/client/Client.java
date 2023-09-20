package nl.rug.aoop.networking.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Communicator;
import nl.rug.aoop.networking.messagehandler.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Client is a class which connects to a Server through a InetSocketAdress. It assumes the Server is already running.
 * Client implements Runnable so that it can be run on a thread.
 */
@Slf4j
public class Client implements Runnable, Communicator {
    private static final int TIMEOUT = 4000;
    private final MessageHandler messageHandler;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket socket;
    @Getter
    private boolean running = false;
    @Getter
    private boolean connected = false;

    /**
     * The Client constructor creates a socket and connects it to the address parameter.
     *
     * @param address        InetSocketAddress used for connecting to the server.
     * @param messageHandler MessageHandler object used for handling messages sent and received through the server.
     * @throws IOException When connection to the address can't be made.
     */
    public Client(InetSocketAddress address, MessageHandler messageHandler) throws IOException {
        this.messageHandler = messageHandler;
        socket = new Socket();
        init(address);
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Method for listening for output from the server and sending it to the messageHandler.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                String input = in.readLine();
                if (input == null) {
                    terminate();
                    break;
                }
                messageHandler.handleMessage(input, this);
            } catch (IOException e) {
                log.error("An error has occurred receiving a message from the server");
                running = false;
                break;
            }
        }
    }

    /**
     * Method for sending a message from the client to the server.
     *
     * @param message JSON String representation of the Message object to be sent.
     */
    @Override
    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    /**
     * Method for terminating the run process and closing the socket.
     */
    public void terminate() {
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Client socket could not be closed", e);
        }
        connected = false;
        log.info("Client process has been terminated.");
    }

    /**
     * Method for connecting to an address through a socket.
     *
     * @param address address used for connecting.
     */
    public void init(InetSocketAddress address) throws IOException {
        socket.connect(address, TIMEOUT);
        if (socket.isConnected()) {
            connected = true;
        }
    }

}
