package com.zyot.fung.shyn.server;

import com.zyot.fung.shyn.packet.RemoveConnectionPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class Connection implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private EventListener listener;
    private boolean running = false;

    public int id;
    public String playerName;

    public Connection(Socket socket, int id) {
        this.socket = socket;
        this.id = id;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            listener = new EventListener();
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
                } catch (ClassNotFoundException | EOFException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!socket.isClosed()) {
            close();

        }
    }

    public void close() {
        try {
            if (this.playerName != null) {
                for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                    Connection c = entry.getValue();
                    if (c.id != this.id) {
                        RemoveConnectionPacket removeConnectionPacket = new RemoveConnectionPacket(this.id, this.playerName);
                        c.sendObject(removeConnectionPacket);
                    }
                }
            }
            running = false;
            in.close();
            out.close();
            socket.close();
            ConnectionHandler.connections.remove(this.id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
