package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.general.ChatRoom;

import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by btemperli on 24.03.15.
 */
public class ConnectionHandler extends Thread {

    private final Socket socket;
    private final Out out;
    private final In in;
    // message holds notifications which will be sent to all active clients;
    // i.e. not only incoming messages, but also add/remove topic requests and
    // add/remove participant requests.

    private String message;
    private ChatRoom chatRoom;
    private static Logger logger;

    private Boolean running;

    public ConnectionHandler(Socket socket) {

        logger = Logger.getLogger(ConnectionHandler.class);
        logger.info("ConnectionHandler initialized");
        this.in = new In(socket);
        this.out = new Out(socket);

        this.socket = socket;
        running = true;
    }

    public void run() {
        logger.info("ConnectionHandler.run()");

        String input = in.readLine();

        while(running && input != null) {
            logger.info(input);
            handleInput(input);

            input = in.readLine();
        }

    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public void handleInput(String input) {
        String key = input.split("=")[0];
        String value = input.split("=")[1];

        if (key.equals("name")) {
            try {
                chatRoom.addParticipant(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("Sorry, but the key (" + key + ") could not be handled.");
        }
    }
}
