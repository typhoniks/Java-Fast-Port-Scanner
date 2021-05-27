package com.company;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

//Developed by github.com/tmvz
public class Main {

    public static void main(final String... args) throws ExecutionException, InterruptedException {
        ArrayList<String> totalPorts = new ArrayList<String>();


        final ExecutorService es = Executors.newFixedThreadPool(20);  //20 threads is optimal /  more threads = +faster
        Scanner getInput = new Scanner(System.in);
        System.out.println("ENTER IP ADRESS : ");
        String ipAdress = getInput.nextLine();
        System.out.println("Please wait . . . .");
        final int myTimeout = 200;
        final List<Future<String>> futures = new ArrayList<>();
        for (int myPort = 1; myPort <= 65535; myPort++) {
            futures.add(myPortIsOpen(es, ipAdress, myPort, myTimeout));
        }
        es.shutdown();
        int openPorts = 0;
        for (final Future<String> f : futures) {
            if (f.get().length() > 0) {
                openPorts++;
                totalPorts.add(f.get() + "");


            }
        }
        System.out.println("--------DONE--------");
        System.out.println("IP ADRESS   : " + ipAdress);
        System.out.println("OPEN PORT   : " + openPorts);
        System.out.println("TIME OUT    : " + myTimeout);
        for (String openPort : totalPorts) {                           //iterate open ports 1by1
            System.out.println("OPEN : " + openPort);
        }
        System.out.println("--------DONE--------");
        System.out.println("Port Protocol Operations : https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers");
    }

    public static Future<String> myPortIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
        return es.submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    int tempPort;
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ip, port), timeout);
                    tempPort = socket.getPort();
                    socket.close();
                    return "" + tempPort;           //returns open port
                } catch (Exception ex) {
                    return "";
                }
            }
        });
    }


}

