package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class BlockServer {
    public static void main(String[] args) throws IOException
    {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(true);

        while(true)
        {
            SocketChannel socketChannel=serverSocketChannel.accept();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int bytesRead=socketChannel.read(byteBuffer);
            while(bytesRead>0)
            {
                byteBuffer.flip();
                System.out.println(StandardCharsets.UTF_8.decode(byteBuffer).toString());
                byteBuffer.clear();
                bytesRead=socketChannel.read(byteBuffer);
            }
            socketChannel.close();
        }
    }
}
