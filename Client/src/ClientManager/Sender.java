package ClientManager;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;

public class Sender {
    private final int PACKET = 1024;
    private final int DATA = PACKET - 1;
    private final Client client;
    public Sender(Client tcpClient) {
        this.client = tcpClient;
    }
    public void send(byte[] data)  {
        for(;;) {
            try {
                while (client.getSocketChannel().isConnectionPending()){
                    client.getSocketChannel().finishConnect();
                }
                while (!client.isConnected()){
                    System.out.print("не подключено, ожидание...");
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                byte[][] ret = new byte[(int) Math.ceil(data.length / (double) DATA)][DATA];
                int start = 0;
                for (int i = 0; i < ret.length; i++) {
                    ret[i] = Arrays.copyOfRange(data, start, start + DATA);
                    start += DATA;
                }
                for (int i = 0; i < ret.length; i++) {
                    var bytes = ret[i];
                    try (var outputStream = new ByteArrayOutputStream()) {
                        outputStream.write(bytes);
                        if (i == ret.length - 1) {
                            outputStream.write(new byte[]{1});
                            client.getSocketChannel().write(ByteBuffer.wrap(outputStream.toByteArray()));
                        } else {
                            outputStream.write(new byte[]{0});
                            client.getSocketChannel().write(ByteBuffer.wrap(outputStream.toByteArray()));
                        }
                    }
                }
                return;
            }
            catch (IOException e) {
                if(!e.getMessage().equals("Соединение разорвано"))
                    System.out.println(e);
            }
        }
    }
}