package ch.fhnw.kvan.chat.socket.server;

import org.apache.log4j.Logger;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by btemperli on 24.03.15.
 */
public class ConnectionListener extends Thread {

    CopyOnWriteArrayList<ConnectionHandler> connections;
    private static Logger logger;
    private Boolean running;

    public ConnectionListener()
    {
        logger = Logger.getLogger(Server.class);
        logger.info("ConnectionListener");
        connections = new CopyOnWriteArrayList<ConnectionHandler>();
        running = true;
    }

    public void addConnection(ConnectionHandler handler)
    {
        connections.add(handler);
    }

    public void run()
    {
        logger.info("run the ConnectionListener");

        while(running) {

            if (connections.size() > 0) {

                checkMessages();

            }
        }
    }

    public void checkMessages() {

        String message;
        for (ConnectionHandler connection : connections) {
            if (connection.getMessage() != null) {
                message = connection.getMessage();
                connection.setMessage(null);

                handleMessage(message, connection);
            }
        }
    }

    public void handleMessage(String message, ConnectionHandler connectionHandler) {
        String key = message.split("=")[0];
        String value = message.split("=")[1];

        if (key.equals("name")) {
            connectionHandler.setParticipant(value);
            for (ConnectionHandler connection : connections) {
                connection.sendParticipants();
            }
        } else if (key.equals("remove_name")) {
            connectionHandler.removeParticipant(value);
            for (ConnectionHandler connection : connections) {
                connection.sendParticipants();
            }
        } else {
            logger.error("Sorry, but the key (" + key + ") could not be handled.");
        }
    }

}
