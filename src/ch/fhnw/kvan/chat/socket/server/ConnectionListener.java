package ch.fhnw.kvan.chat.socket.server;

import org.apache.log4j.Logger;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by btemperli on 24.03.15.
 */
public class ConnectionListener extends Thread {

    CopyOnWriteArrayList<ConnectionHandler> connections;
    private static Logger logger;

    public ConnectionListener()
    {
        logger = Logger.getLogger(Server.class);
        logger.info("ConnectionListener");
        connections = new CopyOnWriteArrayList<ConnectionHandler>();
    }

    public void addConnection(ConnectionHandler handler)
    {
        logger.info("Add a ConnectionHandler");
        connections.add(handler);
    }

    public void run()
    {
        logger.info("run the ConnectionListener");

        while(true) {

            if (connections.size() > 0) {

//                logger.info("ConnectionListener is running");
            }
        }
    }

}
