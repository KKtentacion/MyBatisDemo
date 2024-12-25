package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class ChatClient02 {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost",8080));

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        Thread sendMessageThread=new Thread(()->{
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    System.out.println("输入客户端消息");
                    String msg= bufferedReader.readLine();
                    if(socketChannel.isConnected()){
                        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                        socketChannel.write(buffer);
                    }
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        sendMessageThread.start();

        while (true)
        {
            int readyChannels=selector.select();
            if(readyChannels==0)
            {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while(iterator.hasNext())
            {
                SelectionKey key = iterator.next();
                if(key.isConnectable())
                {
                    socketChannel.finishConnect();
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    System.out.println("连接到服务器");
                }
                else if(key.isReadable()){
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int byteRead=socketChannel.read(buffer);
                    if(byteRead>0){
                        buffer.flip();
                        System.out.println("服务器端消息"+StandardCharsets.UTF_8.decode(buffer).toString().trim());
                    }
                }
                iterator.remove();
            }
        }
    }
}
