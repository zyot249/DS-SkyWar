package com.zyot.fung.shyn.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static com.zyot.fung.shyn.common.Constants.HOST_PORT;

public class Server implements Runnable{
    private int port;
    private int backlog;
    private ServerSocket serverSocket;
    private boolean running = false;
    private int id = 0;

    public Server() {
        this.port = HOST_PORT;
        this.backlog = 100;

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(this.port, this.backlog, inetAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(this).start();
    }

    private void initSocket(Socket socket) {
        Connection connection = new Connection(socket, id);
        ConnectionHandler.connections.put(id, connection);
        new Thread(connection).start();
        id++;
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Server started on port: " + port);

        while (running) {
            try {
                Socket socket = serverSocket.accept();
                initSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        shutdown();
    }

    public void shutdown() {
        running = false;

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
