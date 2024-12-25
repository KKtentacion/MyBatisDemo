package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ChatServer02 {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost",8080));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("聊天室服务端启动了");

        AtomicReference<SocketChannel> clientRef = new AtomicReference<>();

        Thread sendMessageThread=new Thread(()->{
            try (BufferedReader reader=new BufferedReader(new InputStreamReader(System.in))){
                while (true){
                    System.out.println("输入服务器信息");
                    String msg=reader.readLine();
                    SocketChannel socketChannel=clientRef.get();
                    if(socketChannel!=null&&socketChannel.isConnected()){
                        ByteBuffer buffer=ByteBuffer.wrap(msg.getBytes());
                        socketChannel.write(buffer);
                    }
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        });

        sendMessageThread.start();

        while (true)
        {
            int readChannels=selector.select();
            if(readChannels==0){
                continue;
            }

            Set<SelectionKey> selectionKeys=selector.selectedKeys();
            Iterator<SelectionKey> iterator=selectionKeys.iterator();

            while(iterator.hasNext()){
                SelectionKey key=iterator.next();
                if(key.isAcceptable()){
                    SocketChannel client=serverSocketChannel.accept();
                    System.out.println("客户端已连接");
                    client.configureBlocking(false);
                    client.register(selector,SelectionKey.OP_READ);
                    clientRef.set(client);
                }
                else if(key.isReadable()){
                    SocketChannel client=(SocketChannel)key.channel();
                    ByteBuffer buffer=ByteBuffer.allocate(1024);
                    int bytesRead=client.read(buffer);

                    if(bytesRead>0){
                        buffer.flip();
                        String message = new String(StandardCharsets.UTF_8.decode(buffer).toString().trim());
                        System.out.println("客户端消息: " + message);
                    }
                }
                iterator.remove();
            }
        }
    }
}
