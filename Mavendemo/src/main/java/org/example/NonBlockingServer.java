package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NonBlockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress("localhost", 8080));
        serverSocket.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator=selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key=iterator.next();
                iterator.remove();
                if(key.isAcceptable()) {
                    SocketChannel socketChannel=serverSocket.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                if(key.isReadable()) {
                    SocketChannel socketChannel=(SocketChannel)key.channel();
                    ByteBuffer buffer=ByteBuffer.allocate(1024);
                    int bytesRead=socketChannel.read(buffer);
                    if(bytesRead>0) {
                        buffer.flip();
                        System.out.println(StandardCharsets.UTF_8.decode(buffer).toString());
                        buffer.clear();
                    }
                    else {
                        key.cancel();
                        socketChannel.close();
                    }
                }
            }
        }

    }
}
