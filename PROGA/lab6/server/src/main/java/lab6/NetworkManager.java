package lab6;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class NetworkManager {
    private final DatagramSocket socket;
    private final int port;

    public NetworkManager(int port) throws SocketException, UnknownHostException {
        this.port = port;
        socket = new DatagramSocket(port, InetAddress.getLocalHost());
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

    public DatagramPacket receivePacket() throws NetworkException {
        try {
            byte[] buffer = new byte[65500];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            socket.receive(dp);
            return dp;
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }


    public static byte[] serializeObject(Object object){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            objectOutputStream.close();
            return bytes;
        } catch (IOException e){
            throw new RuntimeException("Ошибка сереализации объекта!");
        }
    }

    public static Object deserialazeObject(byte[] data){
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();

            objectInputStream.close();
            return obj;
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("Ошибка десериализации объекта!");
        }
    }
}
