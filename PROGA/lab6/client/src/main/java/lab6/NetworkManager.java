package lab6;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class NetworkManager {
    private final DatagramSocket socket;
    private final int port;

    private final InetAddress serverAdress;
    private final int serverPort;

    public NetworkManager(int port, int serverPort, InetAddress serverAdress) throws SocketException {
        this.port = port;
        this.serverPort = serverPort;
        this.serverAdress = serverAdress;
        socket = new DatagramSocket(port);
        socket.setSoTimeout(8000);
    }

    private void sendPacket(DatagramPacket dp) throws NetworkException {
        try {
            this.socket.send(dp);
        } catch (IOException e) {
            throw new NetworkException("Не удалось отправить пакет!");
        }
    }

    public void sendData(byte[] data, InetAddress address, int port) throws NetworkException {
        DatagramPacket dp = new DatagramPacket(data, data.length, address, port);
        sendPacket(dp);
    }

    public void sendData(byte[] data) throws NetworkException {
        DatagramPacket dp = new DatagramPacket(data, data.length, this.serverAdress, this.serverPort);
        sendPacket(dp);
    }

    public byte[] receiveData() throws NetworkException {
        try {
            byte[] buffer = new byte[65500];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            socket.receive(dp);
            byte[] data = Arrays.copyOf(buffer, dp.getLength());
            return data;
        } catch (IOException e) {
            throw new NetworkException("Сервер не доступен!");
        }
    }

    public static byte[] serializeObject(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            objectOutputStream.close();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сереализации объекта!");
        }
    }

    public static Object deserialazeObject(byte[] data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();

            objectInputStream.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка десериализации объекта!");
        }
    }
}
