package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NonBlockingClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socket = SocketChannel.open();
        socket.configureBlocking(false);
        socket.connect(new InetSocketAddress("localhost", 8080));

        while(!socket.finishConnect())
        {
            System.out.println("Waiting for connection...");
        }

        ByteBuffer buffer=ByteBuffer.allocate(1024);
        buffer.put("Here comes from Client".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        socket.write(buffer);
        buffer.clear();
        socket.close();
    }
}
