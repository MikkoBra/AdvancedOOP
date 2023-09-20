package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Communicator;
import nl.rug.aoop.networking.messagehandler.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientHandler is a class that defines functionality for handling communication with a Client on a Socket.
 */
@Slf4j
public class ClientHandler implements Runnable, Communicator {
    @Getter
    private final Socket socket;
    @Getter
    private final MessageHandler messageHandler;
    private final BufferedReader in;
    private final PrintWriter out;
    @Getter
    private boolean running = false;

    /**
     * Constructor, stores the communication socket and a message handler. Additionally, creates a reader for
     * communication with the client.
     *
     * @param socket         Socket to be communicated over.
     * @param messageHandler MessageHandler that handles incoming messages.
     */
    public ClientHandler(Socket socket, MessageHandler messageHandler) throws IOException {
        this.socket = socket;
        this.messageHandler = messageHandler;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }

    /**
     * Method that runs the ClientHandler by reading client input and sending it to the message handler.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                String line = in.readLine();
                if (line == null) {
                    terminate();
                    break;
                }
                messageHandler.handleMessage(line, this);
            } catch (IOException e) {
                log.error("An error occurred while reading input from client.", e);
                running = false;
                break;
            }
        }
    }

    /**
     * Method that terminates the client connection by terminating the run process and closing the socket.
     */
    public void terminate() {
        running = false;

        try {
            socket.close();
        } catch (IOException e) {
            log.error("ClientHandler socket could not be closed", e);
        }
    }

    @Override
    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }
}
