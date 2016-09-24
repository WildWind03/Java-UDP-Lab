package com.chirikhin.network1udp;

public class Main {
    public static void main(String[] args) {

        try {
            Client client;
            client = new Client(1234, 1000);
            client.run();
        } catch (ClientCreateException e) {
            System.err.print("ERROR: " + e.getMessage() + "\n");
        }

    }
}
