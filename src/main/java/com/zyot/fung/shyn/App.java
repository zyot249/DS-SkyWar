package com.zyot.fung.shyn;

import com.zyot.fung.shyn.client.Client;
import com.zyot.fung.shyn.packet.AddConnectionPacket;
import com.zyot.fung.shyn.server.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

import static com.zyot.fung.shyn.common.Constants.HOST_PORT;

public class App {
    public static void main( String[] args ) {
        boolean running = true;
        while (running) {
            printMenu();
            running = handleUserOption();
        }
    }

    private static boolean handleUserOption() {
        try {
            Scanner scanner = new Scanner(System.in);
            int opt = scanner.nextInt();
            switch (opt) {
                case 1: {
                    createNewGame();
                    break;
                }
                case 2: {
                    joinGame();
                    break;
                }
                case 3: {
                    System.out.println("Thank you for using!");
                    return false;
                }
                default:
                    System.out.println("PLEASE choose option from 1 -> 3!");
            }
            return true;
        } catch (InputMismatchException e) {
            System.out.println("PLEASE choose option from 1 -> 3!");
            return true;
        }
    }

    private static void joinGame() {
        Scanner scanner = new Scanner(System.in);
        String playerName = enterPlayerName(scanner);
        Client client = new Client("localhost", HOST_PORT);
        client.playerName = playerName;
        client.connect();

        try {
            AddConnectionPacket addConnectionPacket = new AddConnectionPacket(playerName);
            client.sendObject(addConnectionPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                client.close();
                break;
            }
        }
    }

    private static String enterPlayerName(Scanner scanner) {
        boolean isDone = false;
        String name = "";
        do {
            try {
                System.out.print("Enter your name: ");
                name = scanner.nextLine();
                if (name.length() > 16) {
                    System.out.println("Name length must be less than 16!");
                } else {
                    isDone = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Name is unacceptable!");
            }
        } while (!isDone);
        return name;
    }

    private static void createNewGame() {
        Server server = new Server(HOST_PORT);
        server.start();

        joinGame();
    }

    private static void printMenu() {
        System.out.print(new StringBuilder()
                .append("1. Create new game\n")
                .append("2. Join game\n")
                .append("3. Quit\n")
                .append("Your choice: ")
                .toString()
        );
    }
}
