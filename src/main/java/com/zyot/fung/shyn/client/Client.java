package com.zyot.fung.shyn.client;

import com.zyot.fung.shyn.packet.ClosedServerNotificationPacket;
import com.zyot.fung.shyn.packet.RemoveConnectionPacket;
import com.zyot.fung.shyn.packet.ServerNotFoundPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Client implements Runnable{
    private String host;
    private int port;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private boolean running = false;
    private EventListener listener;

    private int id;
    public String playerName;

    private boolean isServerDied = false;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.id = -1;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            listener = new EventListener();
            new Thread(this).start();
        } catch (ConnectException e) {
            System.out.println("Unable to connect to the server");
            ServerNotFoundPacket serverNotFoundPacket = new ServerNotFoundPacket();
            serverNotFoundPacket.message = "Unable to connect to the server!";
            EventBuz.getInstance().post(serverNotFoundPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            running = false;
            if (socket != null) {
                if (!socket.isClosed() && this.id != -1 && !isServerDied) {
                    RemoveConnectionPacket removeConnectionPacket = new RemoveConnectionPacket(this.id, this.playerName);
                    sendObject(removeConnectionPacket);
                }

                if (!socket.isClosed()) {
                    in.close();
                    out.close();
                    socket.close();
                    ConnectionHandler.connections.remove(this.id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object packet) {
        if (out == null) {
            return;
        }
        try {
            out.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            running = true;

            while (running) {
                try {
                    Object data = in.readObject();
                    listener.received(data, this);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                    close();
                } catch (EOFException e) {
                    e.printStackTrace();
                    isServerDied = true;
                    close();
                    System.out.println("Disconnected from server!");
                    EventBuz.getInstance().post(new ClosedServerNotificationPacket("Room Master was out!"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
