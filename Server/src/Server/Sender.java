package Server;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;
import java.io.ByteArrayOutputStream;

public class Sender {
    public void send(SocketChannel socketChannel, byte[] data) {
        try {
            int PACKET = 1024;
            int DATA = PACKET - 1;
            byte[][] ret = new byte[(int) Math.ceil(data.length / (double) DATA)][DATA];

            int start = 0;
            for (int i = 0; i < ret.length; i++) {
                ret[i] = Arrays.copyOfRange(data, start, start + DATA);
                start += DATA;
            }
            for (int i = 0; i < ret.length; i++) {
                var bytes = ret[i];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                outputStream.write(bytes);
                outputStream.write(new byte[]{(byte)(i == ret.length - 1?1:0)});
                socketChannel.write(ByteBuffer.wrap(outputStream.toByteArray()));
            }
        }
        catch (IOException e){
            if(Objects.equals(e.getMessage(), "Соединение установлено"))
            {
                try{
                    Thread.sleep(500);
                    socketChannel.close();
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }
}