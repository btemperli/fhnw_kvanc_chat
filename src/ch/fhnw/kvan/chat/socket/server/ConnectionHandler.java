package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.general.ChatRoom;

import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import org.apache.log4j.Logger;
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

    public ConnectionHandler(Socket socket) {

        logger = Logger.getLogger(ConnectionHandler.class
        );
        logger.info("ConnectionHandler initialized");
        in = new In(socket);
        out = new Out(socket);
        this.socket = socket;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
