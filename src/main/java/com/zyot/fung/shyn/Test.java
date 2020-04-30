package com.zyot.fung.shyn;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<String> clients = new ArrayList<>(4);
        clients.add("Dung");
        clients.add("Ngoc");
        clients.add("Phung");
        clients.add("Tung");
        System.out.println("Index of Dung is : " + clients.indexOf("Dung"));

        clients.remove("Dung");
        System.out.println(clients.get(0));
    }
}
