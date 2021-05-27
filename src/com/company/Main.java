package com.company;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

//github.com/tmvz
public class Main {

    public static void main(final String... args) throws ExecutionException, InterruptedException {

        final ExecutorService es = Executors.newFixedThreadPool(20);
        Scanner getInput = new Scanner(System.in);
        System.out.println("ENTER IP ADRESS : ");
        String ipAdress = getInput.nextLine();
        final int myTimeout = 200;
        final List<Future<String>> futures = new ArrayList<>();
        for (int myPort = 1; myPort <= 65535; myPort++) {
            futures.add(myPortIsOpen(es, ipAdress, myPort, myTimeout));
        }
        es.shutdown();
        int openPorts = 0;
        for (final Future<String> f : futures) {
            if (f.get().equals("a")) {
                openPorts++;

            }
        }
        System.out.println("IP ADRESS   : " + ipAdress);
        System.out.println("OPEN PORT   : " + openPorts);
        System.out.println("TIME OUT    : " + myTimeout);
    }

    public static Future<String> myPortIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
        return es.submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), timeout);
                    socket.close();
                    return "a";
                } catch (Exception ex) {
                    return "b";
                }
            }
        });
    }


}

class ScanResult {
    private final int port;
    private final boolean isOpen;
    ArrayList<Integer> totalPorts = new ArrayList<Integer>();

    public ScanResult(int port, boolean isOpen) {
        this.port = port;
        this.isOpen = isOpen;
    }

    public int getPort() {
        return port;
    }

    public boolean isOpen() {
        return isOpen;
    }
}

