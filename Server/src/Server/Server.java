package Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
public class Server {
    public interface TCPExecute { Object Execute(String s, Object o); }
    private int PORT;
    private final HashSet<SocketChannel> sessions;
    private final Receiver receiver = new Receiver();
    private final Sender sender = new Sender();
    private Selector selector;
    private final TCPExecute executer;
    public Server(int port, TCPExecute obj) {
        this.PORT = port;
        executer = obj;
        this.sessions = new HashSet<>();
    }
    public void start() {
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            var socketAddress = new InetSocketAddress("localhost", PORT);
            serverSocketChannel.bind(socketAddress, PORT);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) continue;
                    if (key.isAcceptable()) {accept(key);
                    }else if (key.isReadable()) {
                        var result = receiver.read(key);
                        if(result == null)
                            continue;
                        var res = result;
                        int p = -1;
                        if (res.equals(null))
                            continue;
                        for (int i = res.length - 1; i > -1; i--) {
                            if (res[i] != 0) {
                                p = i;
                                break;
                            }
                        }
                        var cutres = Arrays.copyOfRange(res, 0, p+1);
                        try(var command = new ObjectInputStream(new ByteArrayInputStream(cutres))){
                            var ret = executer.Execute(command.readUTF(), command.readObject());
                            try(var baos = new ByteArrayOutputStream();
                                var a=new ObjectOutputStream(baos)) {
                                a.writeObject(ret);
                                sender.send((SocketChannel) key.channel(), baos.toByteArray());
                            }
                        } catch (Exception e) {
                            sender.send((SocketChannel) key.channel(), "503".getBytes());
                        }
                    }
                }
            }
        } catch (IOException e) {
            if (e.getMessage().equals("Адрес занят")) {
                PORT = (PORT +1)%32767;
                start();
            }
        }
    }
    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            sessions.add(channel);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}