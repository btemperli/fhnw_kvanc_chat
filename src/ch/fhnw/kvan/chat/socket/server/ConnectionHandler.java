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
    private ConnectionListener listener;

    private Boolean running;

    public ConnectionHandler(Socket socket, ConnectionListener listener) {

        logger = Logger.getLogger(ConnectionHandler.class);
        logger.info("ConnectionHandler initialized");
        this.in = new In(socket);
        this.out = new Out(socket);

        this.socket = socket;
        running = true;
    }

    public void run() {

        String input = in.readLine();

        while(running && input != null) {
            message = input;
            input = in.readLine();
        }

    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setParticipant(String name) {
        try {
            chatRoom.addParticipant(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeParticipant(String name) {
        try {
            chatRoom.removeParticipant(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendParticipants() {
        try {
            out.println(chatRoom.getParticipants());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
