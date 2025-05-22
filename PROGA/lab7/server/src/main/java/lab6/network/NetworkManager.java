package lab6.network;

import common.network.exceptions.NetworkException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NetworkManager {
    public void sendData(byte[] data, SocketChannel channel) throws NetworkException {
        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();
        try {
            channel.write(buffer);
        } catch (IOException e) {
            throw new NetworkException("Не удалось отправить данные!");
        }
    }

    public byte[] readFromChannel(SocketChannel channel) throws NetworkException {
        try {
            ByteBuffer lenBuffer = ByteBuffer.allocate(4);
            channel.read(lenBuffer);
            lenBuffer.flip();
            int length = lenBuffer.getInt();

            ByteBuffer buffer = ByteBuffer.allocate(length);
            channel.read(buffer);
            buffer.flip();
            return buffer.array();
        } catch (IOException e) {
            throw new NetworkException("Не удалось прочитать данные из канала!");
        }
    }
}
