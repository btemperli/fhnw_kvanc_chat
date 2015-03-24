package ch.fhnw.kvan.chat.socket.client;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by btemperli on 24.03.15.
 */
public class Client {

    private static Logger logger;
    private static Socket socket;
    private static In in;
    private static Out out;


    public static void main(String args[]) {

        logger = Logger.getLogger(Client.class);

        logger.info("TODO:");
        logger.info("Start a client with " + args.length + " arguments");
        for (String arg : args) {
            logger.info(arg);
        }

        try {
            socket = new Socket("localhost", 8080);
            in = new In(socket);
            out = new Out(socket);

            IChatRoom chatRoom = ChatRoom.getInstance();

            ClientGUI gui = new ClientGUI(chatRoom, args[0]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
