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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by btemperli on 24.03.15.
 */
public class Client implements IChatRoom {

    private static Logger logger;
    private static Socket socket;
    private static In in;
    private static Out out;
    private static Boolean running;
    private static ClientGUI gui;
    private static ChatRoom chatRoom;
    private static String name;


    public static void main(String[] args) {
        logger = Logger.getLogger(Client.class);

        name = args[0];
        Client client = new Client(args[1], Integer.parseInt(args[2]));

    }

    public Client(String host, int port) {

        logger.info("Start a client");

        try {
            socket = new Socket(host, port);
            in = new In(socket);
            out = new Out(socket);

            // do the login, send the name to the server.
            out.println("name=" + name);

            chatRoom = ChatRoom.getInstance();

            ClientWindowListener listener = new ClientWindowListener() {
                @Override
                public void callListenerWindowClose() {
                    exitWindow();
                }
            };

            gui = new ClientGUI(this, name, listener);

            running = true;

            startListener();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startListener() {

        new Thread() {
            public void run() {

                String input = in.readLine();

                while(running && input != null) {
                    handleInput(input);
                    input = in.readLine();
                }
            }
        }.start();
    }

    public void handleInput(String input) {
        logger.info(input);

        String key = input.split("=")[0];
        String value = "";
        try {
            value = input.split("=")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            // do nothing
        }

        if (key.equals("participants")) {
            addParticipant(value);
        } else if (key.equals("add_topic")) {
            addTopicFromServer(value);
        } else if (key.equals("topics")) {
            String[] values = value.split(";");
            for (String topic : values) {
                addTopicFromServer(topic);
            }
        } else if (key.equals("message")) {
            String message = value.split(";")[0];
            String topic = input.split("=")[2];
            addMessageFromServer(topic, message, true);
        } else if (key.equals("messages")) {
            String[] values = value.split(";");
            for (String message : values) {
                addMessageFromServer(gui.getCurrentTopic(), message, false);
            }
        } else {
            logger.error("Sorry, but the key (" + key + ") could not be handled.");
        }
    }

    /**
     * Listener for exitWindow, logs out the client from the server.
     */
    public static void exitWindow() {
        System.out.println("GUI is now closed...");
        out.println("remove_name=" + name);
    }

    public String getMessages(String topic) {

        out.println("get_messages=" + topic);
        return null;
    }

    public boolean addMessage(String topic, String message) {
        logger.info("client: add Message: " + message + " in topic " + topic);

        out.println("message=" + message + ";topic=" + topic);

        return false;
    }

    public boolean removeTopic(String topic) {
        logger.info("client: remove Topic: " + topic);
        out.println("remove_topic=" + topic);
        return false;
    }

    /**
     * Add new Topic by HandleInput
     * @param value topic-name
     */
    public boolean addTopic (String value) {
        logger.info("client: add Topic: " + value);

        out.println("add_topic=" + value);
        return true;
    }

    public String refresh(String topic) {
        logger.info("Refresh");
        gui.updateMessages(null);

        return getMessages(topic);
    }

    public boolean removeParticipant(String value) {
        logger.info("client: remove Participant: " + value);
        return false;
    }

    /**
     * Add Participants
     * - to the gui
     * - to the client's chatroom
     * @param value String, comes directly from the server
     */
    public boolean addParticipant(String value) {
        try {
            String[] names = value.split(";");
            List<String> namesList = new ArrayList<String>(Arrays.asList(names));
            namesList.remove(name); // remove own name from list.

            String participants = chatRoom.getParticipants();

            if (!participants.equals("participants=")) {

                // there are participants yet. remove the old from the new list
                String[] oldNames = participants.split("=")[1].split(";");

                if (oldNames.length < names.length) {
                    for (String oldName : oldNames) {
                        namesList.remove(oldName);
                    }

                    // now add the new ones.
                    for (String name : namesList) {
                        gui.addParticipant(name);
                        chatRoom.addParticipant(name);
                    }
                } else {
                    for (String oldName : oldNames) {
                        if (!oldName.equals(name) && namesList.indexOf(oldName) == -1) {
                            gui.removeParticipant(oldName);
                            chatRoom.removeParticipant(oldName);
                        }
                    }
                }
            } else {

                // there are no participants. add all.
                for (String name : namesList) {
                    gui.addParticipant(name);
                    chatRoom.addParticipant(name);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void addTopicFromServer(String topic) {
        logger.info("Server sent new Topic: " + topic);

        if (!topic.equals("")) {
            try {
                chatRoom.addTopic(topic);
                gui.addTopic(topic);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMessageFromServer(String topic, String message, Boolean newMessage) {
        if (!message.equals("")) {
            try {
                if (newMessage) {
                    chatRoom.addMessage(topic, message);
                }
                if (gui.getCurrentTopic().equals(topic)) {
                    gui.addMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}