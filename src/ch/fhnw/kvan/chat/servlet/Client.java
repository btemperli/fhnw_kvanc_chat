package ch.fhnw.kvan.chat.servlet;

import org.apache.log4j.Logger;

/**
 * Created by btemperli on 24.03.15.
 */
public class Client {

    private static Logger logger;

    public static void main(String args[]) {

        logger = Logger.getLogger(Client.class);

        logger.info("TODO:");
        logger.info("servlet.Client");
        logger.info("Start a client with " + args.length + " arguments");
        for (String arg : args) {
            logger.info(arg);
        }
    }

}
