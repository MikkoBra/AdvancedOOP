package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.messagehandler.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Server is an implementation of a network server that defines functionality for accepting a Client connection,
 * creating a Socket and creating a dedicated ClientHandler on the Socket that communicates with the Client.
 */
@Slf4j
public class Server implements Runnable {
    @Getter
    private final MessageHandler messageHandler;
    @Getter
    private final int port;
    @Getter
    private final List<ClientHandler> clientHandlerList;
    private final ExecutorService executorService;
    private ServerSocket serverSocket;
    @Getter
    private boolean running;

    /**
     * Constructor, creates a ServerSocket, saves its port number, and creates a thread pool.
     *
     * @param port           Integer representing the port on which a server should be created.
     * @param messageHandler Interface which handles the Messages sent through the server.
     */
    public Server(int port, MessageHandler messageHandler) throws IllegalArgumentException {
        this.messageHandler = messageHandler;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            log.error("Something went wrong when connecting ServerSocket to the port", e);
        }
        this.port = serverSocket.getLocalPort();
        executorService = Executors.newCachedThreadPool();
        clientHandlerList = new ArrayList<>();
    }

    /**
     * Method that runs the server by accepting a client connection and creating a ClientHandler with a MessageHandler
     * to handle the client connection on a thread.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket socket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(socket, messageHandler);
                clientHandlerList.add(clientHandler);
                executorService.submit(clientHandler);
            } catch (IOException e) {
                log.error("Something went wrong with spawning the clientHandler.", e);
            }
        }
    }

    /**
     * Method that terminates the whole server by terminating the run process shutting down all threads, and closing
     * the server socket.
     */
    public void terminate() {
        running = false;

        clientHandlerList.forEach(ClientHandler::terminate);
        clientHandlerList.clear();

        executorService.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            log.error("Something went wrong with closing the server.", e);
        }
        try {
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                log.warn("Not all tasks have been finished before terminating");
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting");
        } finally {
            executorService.shutdownNow();
        }
        log.info(String.valueOf(executorService.isShutdown()));
    }

    public int getNumClients() {
        return clientHandlerList.size();
    }
}
