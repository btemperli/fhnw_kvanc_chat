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

    public void handleMessage(String input, ConnectionHandler connectionHandler) {
        String key = input.split("=")[0];
        String value = input.split("=")[1];

        if (key.equals("name")) {
            connectionHandler.setParticipant(value);
            connectionHandler.sendTopics();
            for (ConnectionHandler connection : connections) {
                connection.sendParticipants();
            }
        } else if (key.equals("remove_name")) {
            connectionHandler.removeParticipant(value);
            for (ConnectionHandler connection : connections) {
                connection.sendParticipants();
            }
        } else if (key.equals("add_topic")) {
            for (ConnectionHandler connection : connections) {
                connection.addTopic(value);
            }
        } else if (key.equals("message")) {
            String message = value.split(";")[0];
            String topic = input.split("=")[2];
            connectionHandler.addMessage(message, topic);
            for (ConnectionHandler connection : connections) {
                connection.sendMessage(message, topic);
            }
        } else if (key.equals("get_messages")) {
            connectionHandler.sendMessages(value);
        } else {
            logger.error("Sorry, but the key (" + key + ") could not be handled.");
        }
    }

}
