package com.Kkktentacion;

import org.example.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private final static int PORT =8080;

    public Server() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8080));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server started");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true)
            {
                selector.select();
                Set<SelectionKey> selectedKeys=selector.selectedKeys();
                Iterator<SelectionKey> iterator=selectedKeys.iterator();
                while (iterator.hasNext())
                {
                    SelectionKey key=iterator.next();
                    iterator.remove();
                    HandleKey(key);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void HandleKey(SelectionKey key) throws IOException
    {
        if(key.isAcceptable())
        {
            SocketChannel socketChannel=serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("Connect from"+socketChannel.getRemoteAddress());
            System.out.println("Connected");
        }
        if(key.isReadable())
        {
            SocketChannel socketChannel=(SocketChannel) key.channel();
            Thread thread=new ClientHandler(socketChannel);
            thread.start();
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}

class ClientHandler extends Thread {
    private SocketChannel socketChannel;
    private final static String SuccessAnswer="HTTP/1.1 200 OK\r\n"+
                                                "Connection: close\r\n"+
                                                "Content-Type: text/html\r\n"+
                                                "\r\n";
    private final static String FailAnswer="HTTP/1.1 404 Not Found\r\n"+
                                            "Content-length: 0\r\n"+
                                            "\r\n";


    ClientHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        ByteBuffer inputbuffer = ByteBuffer.allocate(8192);
        StringBuilder requestBuilder=new StringBuilder();
        while (socketChannel.read(inputbuffer) != -1) {
            while(inputbuffer.hasRemaining())
            {
                requestBuilder.append((char) inputbuffer.get());
            }
            inputbuffer.clear();
        }

        String request=requestBuilder.toString();
        System.out.println("Get request");
        HandleRequest(request);
    }

    private void HandleRequest(String request)
    {
        String[] lines = request.split("\r\n");

        for(String s:lines)
        {
            System.out.println(lines);
        }

        String requestLine=lines[0];
        String[] requestParts=requestLine.split(" ");
        String method =requestParts[0];
        String path =requestParts[1];
        String protocol=requestParts[2];

        boolean requestflag=false;
        if(method.equals("GET"))
        {
            requestflag=true;
        }

        ByteBuffer outputbuffer = ByteBuffer.allocate(8192);

        if(requestflag)
        {
            String data="<html><body><h1>Hello, world!</h1></body></html>";
            int length=data.getBytes(StandardCharsets.UTF_8).length;
            outputbuffer.put(SuccessAnswer.getBytes())
        }
    }
}
