package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static void main(String[] args) throws IOException
    {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress( 8080));

        SocketChannel socketChannel = serverSocketChannel.accept();

        ByteBuffer headBuffer = ByteBuffer.allocate(128);
        ByteBuffer bodyBuffer = ByteBuffer.allocate(1024);

        ByteBuffer[] buffers={headBuffer,bodyBuffer};

        long bytesRead=socketChannel.read(buffers);

        headBuffer.flip();
        while(headBuffer.hasRemaining())
        {
            System.out.print((char)headBuffer.get());
        }

        System.out.println();

        bodyBuffer.flip();
        while(bodyBuffer.hasRemaining())
        {
            System.out.print((char) bodyBuffer.get());
        }

        ByteBuffer headerRespons =ByteBuffer.wrap("Header Response".getBytes());
        ByteBuffer bodyRespons = ByteBuffer.wrap("Body Response".getBytes());

        ByteBuffer[] responseBuffers={headerRespons,bodyRespons};

        long bytesWritten=socketChannel.write(responseBuffers);

        socketChannel.close();
        serverSocketChannel.close();
    }
}
