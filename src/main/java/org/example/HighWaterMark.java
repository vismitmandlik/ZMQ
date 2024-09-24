package org.example;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import static org.zeromq.SocketType.*;

public class HighWaterMark {

    private static final int HIGH_WATER_MARK = 7; // High-water mark
    private static final String BIND_ADDRESS = "tcp://*:5555";

    public static void main(String[] args) {
        Thread producer = new Thread(new Producer());
        Thread consumer = new Thread(new Consumer());

        producer.start();
        consumer.start();
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            try (ZContext context = new ZContext()) {
                // Create the socket using the correct method
                ZMQ.Socket socket = context.createSocket(PUSH);
                socket.bind(BIND_ADDRESS);
                socket.setHWM(HIGH_WATER_MARK); // Set the high-water mark

                for (int i = 0; i < 20; i++) {
                    String message = "Message " + i;
                    socket.send(message.getBytes(ZMQ.CHARSET), 0);
                    System.out.println("Produced: " + message);
                     Thread.sleep(200); // Simulate some production time
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            try (ZContext context = new ZContext()) {
                // Create the socket using the correct method
                ZMQ.Socket socket = context.createSocket(PULL);
                socket.connect(BIND_ADDRESS);

                while (true) {
                    byte[] reply = socket.recv(0);
                    String message = new String(reply, ZMQ.CHARSET);
                    System.out.println("Consumed: " + message);
                    Thread.sleep(1000); // Simulate processing time
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
