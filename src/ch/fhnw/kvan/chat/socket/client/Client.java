package ch.fhnw.kvan.chat.socket.client;

import org.apache.log4j.Logger;

/**
 * Created by btemperli on 17.03.15.
 */
public class Client {

    private static Logger logger;

    public static void main(String args[]) {

        logger = Logger.getLogger(Client.class);

        logger.info("Started a client with " + args.length + " arguments");
        for (String arg : args) {
            logger.info(arg);
        }
    }
}
