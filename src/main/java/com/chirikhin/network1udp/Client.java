package com.chirikhin.network1udp;

import java.io.IOException;
import java.net.*;

import java.util.*;
import java.util.logging.Logger;

public class Client extends Observable implements Runnable{
    private final static Logger logger = Logger.getLogger(Client.class.getName());
    private final static int TIME_BETWEEN_SENDINGS = 1000;
    private final static String BROADCAST_IP = "255.255.255.255";

    private final DatagramSocket datagramSocket;

    private final HashMap<InfoAboutOtherPC, InfoAboutOtherPC> infoAboutOtherPCs = new HashMap<>();

    private int port;

    public Client (int port, int timeout) throws ClientCreateException {
        try {
            this.datagramSocket = new DatagramSocket(port);
            this.datagramSocket.setSoTimeout(timeout);
            this.port = port;
        } catch (SocketException e) {
            throw new ClientCreateException(e.getMessage());
        }
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (System.currentTimeMillis() - time > TIME_BETWEEN_SENDINGS) {
                    datagramSocket.send(new DatagramPacket(new byte[]{}, 0, new InetSocketAddress(BROADCAST_IP, port)));
                    time = System.currentTimeMillis();
                }

                DatagramPacket receivePacket = new DatagramPacket(new byte[]{}, 0);

                datagramSocket.receive(receivePacket);

                InfoAboutOtherPC infoAboutOtherPC = new InfoAboutOtherPC(receivePacket.getPort(), receivePacket.getAddress(), System.currentTimeMillis());
                if (!infoAboutOtherPCs.containsKey(infoAboutOtherPC)) {
                    infoAboutOtherPCs.put(infoAboutOtherPC, infoAboutOtherPC);
                    System.out.println(infoAboutOtherPC.getInetAddress() + " was added");
                } else {
                    infoAboutOtherPC = infoAboutOtherPCs.get(infoAboutOtherPC);
                    infoAboutOtherPC.setDate(System.currentTimeMillis());
                }

                Iterator<Map.Entry<InfoAboutOtherPC, InfoAboutOtherPC>> iterator = infoAboutOtherPCs.entrySet().iterator();

                while (iterator.hasNext()) {
                        InfoAboutOtherPC infoAboutOtherPC1 = iterator.next().getKey();
                        if (!infoAboutOtherPC1.isActual(System.currentTimeMillis())) {
                            iterator.remove();
                            System.out.println(infoAboutOtherPC1.getInetAddress() + " was removed");
                        }
                }

            }
            catch (IOException e) {
                //logger.log(Level.INFO, e.getMessage());
            }
        }
    }
}
