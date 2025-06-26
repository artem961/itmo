package lab6.network;

import common.ConfigLoader;
import common.network.exceptions.NetworkException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class NetworkManager implements AutoCloseable {
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;

    public NetworkManager() throws NetworkException {
        try {
           // ConfigLoader configLoader = new ConfigLoader("connection.properties");
           // InetAddress serverAddress = InetAddress.getByName(configLoader.get("server_address"));
           // int serverPort = Integer.parseInt(configLoader.get("server_port"));
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 13536;

            socket = new Socket();
            socket.setReceiveBufferSize(524288*2);
            try {
                socket.connect(new InetSocketAddress(serverAddress, serverPort), 5000);
            } catch (IOException e) {
                throw new NetworkException("Не удалось подключиться к серверу!");
            }
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (IOException e) {
            throw new NetworkException(e.toString());
        }
    }

    public byte[] receiveData() throws NetworkException {
        try {
            byte[] lenBytes = in.readNBytes(4);
            if (lenBytes.length == 0) {
                throw new NetworkException("Ошибка получения длины!");
            }

            int length = ByteBuffer.wrap(lenBytes).getInt();
            return in.readNBytes(length);
        } catch (IOException e) {
            throw new NetworkException("Не удалось прочитать сообщение!");
        }
    }

    public void sendData(byte[] data) throws NetworkException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
            buffer.putInt(data.length);
            buffer.put(data);
            buffer.flip();

            out.write(buffer.array());
        } catch (IOException e) {
            throw new NetworkException("Не удалось отправить данные!");
        }
    }

    @Override
    public void close() throws Exception {
        socket.shutdownInput();
        socket.shutdownOutput();
        in.close();
        out.close();
        socket.close();
    }
}