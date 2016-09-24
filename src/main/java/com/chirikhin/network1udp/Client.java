package com.chirikhin.network1udp;

import java.io.IOException;
import java.net.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.logging.Logger;

public class Client extends Observable implements Runnable{
    private final static Logger logger = Logger.getLogger(Client.class.getName());

    private final DatagramSocket datagramSocket;

    private final LinkedList<InfoAboutOtherPC> infoAboutOtherPCs = new LinkedList<>();

    private int port;

    public Client (int port, int timeout) throws ClientCreateException {
        try {
            this.datagramSocket = new DatagramSocket(port);
            this.datagramSocket.setSoTimeout(timeout);
            this.port = port;
        } catch (SocketException e) {
            e.printStackTrace();
            throw new ClientCreateException(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                datagramSocket.send(new DatagramPacket(new byte[]{}, 0, new InetSocketAddress("255.255.255.255", port)));

                DatagramPacket receivePacket = new DatagramPacket(new byte[]{}, 0);

                datagramSocket.receive(receivePacket);

                InfoAboutOtherPC infoAboutOtherPC = new InfoAboutOtherPC(receivePacket.getPort(), receivePacket.getAddress(), System.currentTimeMillis());
                if (infoAboutOtherPCs.contains(infoAboutOtherPC)) {
                    continue;
                } else {
                    infoAboutOtherPCs.add(infoAboutOtherPC);
                    System.out.println(infoAboutOtherPC.getInetAddress() + " was added");
                }

                Iterator<InfoAboutOtherPC> iterator = infoAboutOtherPCs.iterator();

                while (iterator.hasNext()) {
                        InfoAboutOtherPC infoAboutOtherPC1 = iterator.next();
                        if (!infoAboutOtherPC1.isActual(System.currentTimeMillis())) {
                            iterator.remove();
                            System.out.println(infoAboutOtherPC1.getInetAddress() + " was removed");
                        }
                }

            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
