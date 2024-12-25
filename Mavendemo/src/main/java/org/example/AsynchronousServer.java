package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

public class AsynchronousServer {
    public static void main(String[] args) throws IOException,InterruptedException {
        AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 8080));

        System.out.println("Server started on port 8080");

        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
                serverChannel.accept(null, this);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                Future<Integer> ReadResult = socketChannel.read(buffer);

                try {
                    ReadResult.get();
                    buffer.flip();
                    //String message=new String(buffer.array(),0,buffer.remaining());
                    System.out.println(StandardCharsets.UTF_8.decode(buffer).toString());
                    buffer.clear();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });

        System.out.println("Waiting for client...");

        Thread.currentThread().join();
    }
}
