package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynchronousClient {
    public static void main(String[] args) {
        try {
            AsynchronousSocketChannel client=AsynchronousSocketChannel.open();
            Future<Void> connectResult=client.connect(new InetSocketAddress("localhost",8080));
            connectResult.get();

            String message="Are you sure you want to say hello?";
            ByteBuffer buffer=ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
            Future<Integer> writeResult=client.write(buffer);
            writeResult.get();

            System.out.println("Message sent");
            client.close();
        }
        catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
