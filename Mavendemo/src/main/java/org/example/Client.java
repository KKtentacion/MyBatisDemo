package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8080));

        String header="Header Content";
        String body="Body Content";

        ByteBuffer headerBuffer=ByteBuffer.wrap(header.getBytes());
        ByteBuffer bodyBuffer=ByteBuffer.wrap(body.getBytes());

        ByteBuffer[] buffers={headerBuffer,bodyBuffer};
        socketChannel.write(buffers);

        ByteBuffer headerResponseBuffer=ByteBuffer.allocate(128);
        ByteBuffer bodyResponseBuffer=ByteBuffer.allocate(1024);

        ByteBuffer[] responseBuffers={headerResponseBuffer,bodyResponseBuffer};

        long bytesRead=socketChannel.read(responseBuffers);

        headerResponseBuffer.flip();
        while(headerResponseBuffer.hasRemaining()){
            System.out.print((char) headerResponseBuffer.get());
        }

        bodyResponseBuffer.flip();
        while(bodyResponseBuffer.hasRemaining()){
            System.out.print((char) bodyResponseBuffer.get());
        }

        socketChannel.close();
    }
}
