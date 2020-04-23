package com.zyot.fung.shyn;

import java.util.Scanner;

import com.zyot.fung.shyn.client.main.Client;
import com.zyot.fung.shyn.preference.MyPreference;
import com.zyot.fung.shyn.server.main.ConnectionHandler;
import com.zyot.fung.shyn.server.main.Server;
import com.zyot.fung.shyn.server.packet.request.JoinRoomRequest;

public class App 
{
    public static void main( String[] args )
    {
    	Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 5) {
        	System.out.print("Your choice: ");
        	choice = scanner.nextInt();
        	scanner.nextLine();
            switch (choice) {
            case 1:
            	System.out.print("Enter the name: ");
            	String newName = scanner.nextLine();
            	MyPreference.playerName = newName;
            	System.out.println("Your new name: " + MyPreference.playerName);
                break;
            case 2:
                // Perform "create room" case.
            	createRoom();
                break;
            case 3:
                // Perform "join room" case.

            	joinRoom(false);
                break;
            case 4:
                break;
            default:
            	choice = 5;
            }
        }
        scanner.close();
    }
    
    private static void createRoom() {
    	int port = MyPreference.port;
    	int maxPlayer = 4;
    	ConnectionHandler.maxConcurentConnections = maxPlayer;
    	Server server = new Server(port);
		server.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		joinRoom(true);
    }
    
    private static void joinRoom(Boolean isMaster) {
    	String hostAddr = "localhost";
    	int hostPort = 6969;
    	
    	Client client = new Client(hostAddr,hostPort);
		Boolean connectSuccess = client.connect();
		if (!connectSuccess) {
			System.out.println("Cannot connect to the server: " + hostAddr + ":" + hostPort);
			return;
		}
		
    	String playerName = MyPreference.playerName;
    	JoinRoomRequest joinRequest = new JoinRoomRequest(0, playerName, isMaster);
    	
    	client.sendObject(joinRequest);
    }
}
