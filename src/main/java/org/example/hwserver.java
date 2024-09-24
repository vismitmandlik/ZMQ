package org.example;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.util.Scanner;

public class hwserver
{
    public static void main(String[] args) throws Exception
    {
        Scanner sc = new Scanner(System.in);

        try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://10.20.40.207:5555");
            System.out.println("Connection established " );

            while (!Thread.currentThread().isInterrupted()) {
                byte[] reply = socket.recv(0);
                System.out.println(
                        "Received: " + new String(reply, ZMQ.CHARSET)
                );
                System.out.print("You: ");
                String response = sc.nextLine();
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
        }
    }
}