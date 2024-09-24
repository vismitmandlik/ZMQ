package org.example;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.util.Scanner;

public class hwclient
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        try (ZContext context = new ZContext()) {
            //  Socket to talk to server
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://10.20.40.207:5555");
            System.out.println("Connection established " );

            while (!Thread.currentThread().isInterrupted()) {
                System.out.print("You: ");
                String request = sc.nextLine();
                socket.send(request.getBytes(ZMQ.CHARSET), 0);
                byte[] reply = socket.recv(0);
                System.out.println(
                        "Received: " + new String(reply, ZMQ.CHARSET)
                );
            }
        }
    }
}
