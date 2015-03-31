package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.general.ChatRoomDriver;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by btemperli on 17.03.15.
 */
public class Server {

    private static Logger logger;
    private static Boolean running;
    public ChatRoomDriver chatRoomDriver;
    public ServerSocket serverSocket;

    public static void main(String[] args) {

        BasicConfigurator.configure();
        logger = Logger.getLogger(Server.class);
        running = true;

        Server server = new Server();
    }

    public Server() {
        try {
            chatRoomDriver = new ChatRoomDriver();
            chatRoomDriver.connect("localhost", 8080);
            serverSocket = new ServerSocket(8080);

            // ConnectionListener: Handles all active SocketConnections to the Clients and waits for new Requests
            ConnectionListener listener = new ConnectionListener();
            listener.start();

            while (running) {

                // Server accepts Client --> new Client Socket
                Socket socket = serverSocket.accept();

                // Each Client gets an own thread with reference on the socket
                ConnectionHandler handler = new ConnectionHandler(socket, listener);
                logger.info("Client (" + socket.getInetAddress() + ") connected");
                handler.setChatRoom((ChatRoom) chatRoomDriver.getChatRoom());

                // Add new Connection to the listener
                listener.addConnection(handler);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}