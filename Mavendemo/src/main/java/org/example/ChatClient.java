package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    private Selector selector;
    private SocketChannel socketChannel;
    private static final String HOST="localhost";
    private static final int PORT=8080;

    ChatClient()
    {
        try {
            selector=Selector.open();
            socketChannel=SocketChannel.open(new InetSocketAddress(HOST,PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("Client connected");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        new Thread(()->
        {
            try {
                while(true)
                {
                    if(selector.select()>0)
                    {
                        for(SelectionKey key:selector.selectedKeys())
                        {
                            selector.selectedKeys().remove(key);
                            if(key.isReadable())
                            {
                                readMessage();
                            }
                        }
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();

        try(BufferedReader reader=new BufferedReader(new InputStreamReader(System.in)))
        {
            String Inputmsg;
            while((Inputmsg=reader.readLine())!=null)
            {
                sendMessage(Inputmsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws IOException
    {
        if(message!=null&&!message.trim().isEmpty())
        {
            ByteBuffer buffer=ByteBuffer.wrap(message.getBytes());
            socketChannel.write(buffer);
        }
    }

    private void readMessage() throws IOException
    {
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        int read=socketChannel.read(buffer);
        if(read>0)
        {
            buffer.flip();
            String msg= StandardCharsets.UTF_8.decode(buffer).toString();
            System.out.println(msg);
        }
    }

    public static void main(String[] args)
    {
        new ChatClient().start();
    }
}
