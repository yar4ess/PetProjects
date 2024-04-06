package ClientManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    private InetSocketAddress HOST;
    private SocketChannel socketChannel;
    private Receiver receivingManager = null;
    private Sender sendingManager = null;
    public Client(String adr, int port) {
        this.HOST = new InetSocketAddress(adr, port);
    }

    public void start(String adr, int port) {
        HOST = new InetSocketAddress(adr, port);
        start();
    }

    public void start(String adr) {
        HOST = new InetSocketAddress(adr, HOST.getPort());
        start();
    }
    public void start() {
        for(;;){
            try {
                if(socketChannel != null)
                    socketChannel.close();
                this.socketChannel = SocketChannel.open();
                socketChannel.bind(new InetSocketAddress("127.0.0.1", 22828));
                socketChannel.configureBlocking(false);
                socketChannel.connect(HOST);
                receivingManager = new Receiver(this);
                sendingManager = new Sender(this);
                return;
            } catch (Exception e) {
                System.out.println("TCP client: " + e.getMessage());
                try {
                    socketChannel.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public SocketChannel getSocketChannel () {
        return this.socketChannel;
    }
    public Object send (String s, Object obj) {
        try (var baos = new ByteArrayOutputStream(); var a=new ObjectOutputStream(baos)) {
            a.writeUTF(s);
            a.writeObject(obj);
            sendingManager.send(baos.toByteArray());
            System.out.println();
            try(var command = new ObjectInputStream(new ByteArrayInputStream(receivingManager.receive()))) {
                return command.readObject();
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public boolean isConnected() {
        return socketChannel != null && socketChannel.socket().isBound() && !socketChannel.socket().isClosed() && socketChannel.isConnected()
                && !socketChannel.socket().isInputShutdown() && !socketChannel.socket().isOutputShutdown();
    }
}


