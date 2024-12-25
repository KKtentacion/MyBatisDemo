package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT=8080;

    ChatServer()
    {
        try {
            selector = Selector.open();
            serverSocketChannel = serverSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server started");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        try {
            while(true)
            {
                if(selector.select()>0)
                {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while(iterator.hasNext())
                    {
                        SelectionKey selectionKey=iterator.next();
                        iterator.remove();
                        handleKey(selectionKey);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handleKey(SelectionKey selectionKey) throws IOException
    {
        if(selectionKey.isAcceptable())
        {
            SocketChannel sockectChannel=serverSocketChannel.accept();
            sockectChannel.configureBlocking(false);
            sockectChannel.register(selector,SelectionKey.OP_READ);
            System.out.println("server accepted:"+sockectChannel.getRemoteAddress());
        }
        else if(selectionKey.isReadable())
        {
            SocketChannel sockectChannel=(SocketChannel) selectionKey.channel();
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            int read=sockectChannel.read(buffer);
            if(read>0)
            {
                buffer.flip();
                String msg=StandardCharsets.UTF_8.decode(buffer).toString();
                System.out.println("Client Send:"+msg);
                sockectChannel.write(ByteBuffer.wrap(("Server Reply:"+msg).getBytes()));
            }
        }
    }

    public static void main(String[] args)
    {
        new ChatServer().start();
    }
}


