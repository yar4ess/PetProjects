package ClientManager;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.io.ByteArrayOutputStream;

public class Receiver {
    private byte[] receivedData;
    private final Client client;
    public Receiver(Client tcpClient) {
        this.client = tcpClient;
        this.receivedData = new byte[0];
    }
    public byte[] receive() {
        receivedData = new byte[0];
        for(;;){
            try {
                if(client.getSocketChannel() == null)
                    continue;
                while (client.getSocketChannel().isConnectionPending()){
                    client.getSocketChannel().finishConnect();
                }
                if(!client.getSocketChannel().isOpen()){
                    client.start();
                    Thread.sleep(3000);
                    continue;
                }
                int DATA = 1024;
                ByteBuffer byteBuffer = ByteBuffer.allocate(DATA);
                var readBytes = client.getSocketChannel().read(byteBuffer);
                if(readBytes == 0) {
                    Thread.sleep(50);
                    continue;
                }
                if(readBytes == -1)
                    client.getSocketChannel().close();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write(receivedData);
                outputStream.write(Arrays.copyOf(byteBuffer.array(), byteBuffer.array().length - 1));
                receivedData = outputStream.toByteArray();
                if (byteBuffer.array()[readBytes - 1] == 1) {
                    return receivedData;
                }
                byteBuffer.clear();
            }
            catch (Exception e ) {
                if(Objects.equals(e.getMessage(), "Соединение установлено"))
                {
                    try{
                        Thread.sleep(3000);
                        client.getSocketChannel().close();
                        client.start();
                    } catch (Exception e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
        }
    }
}
