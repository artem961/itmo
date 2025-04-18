package lab6;

import common.network.Constants;
import common.network.NetworkException;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

public class NetworkManager {
    private final DatagramSocket socket;
    private final int port;
    private final int bufferSize = Constants.PACKET_SIZE.getValue();
    private final int packetDataSize = bufferSize - 8;


    private final InetAddress serverAddress;
    private final int serverPort;

    public NetworkManager(int port, int serverPort, InetAddress serverAddress) throws SocketException {
        this.port = port;
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
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

    private DatagramPacket receivePacket() throws NetworkException {
        try {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            socket.receive(dp);
            return dp;
        } catch (IOException e) {
            throw new NetworkException("Сервер не доступен!");
        }
    }

    public byte[] receiveData() throws NetworkException {
        SortedMap<Integer, byte[]> packets = new TreeMap<>();
        int totalCount = -1;

        while (true) {
            DatagramPacket dp = receivePacket();
            byte[] packetData = dp.getData();
            ByteBuffer buffer = ByteBuffer.wrap(packetData, 0, 8);
            int packetNumber = buffer.getInt();
            int count = buffer.getInt();
            if (totalCount == -1) totalCount = count;

            packetData = Arrays.copyOfRange(packetData, 8, dp.getLength());
            packets.put(packetNumber, packetData);
            if (packetNumber == totalCount) break;
        }
        return combineMessage(packets);
    }

    public void sendData(byte[] data, InetAddress address, int port) throws NetworkException {
        int packetsCount = (int) Math.ceil((double) data.length / packetDataSize);
        for (int number = 0; number < packetsCount; number++) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            buffer.putInt(number + 1);
            buffer.putInt(packetsCount);
            buffer.put(data, number* packetDataSize, Math.min(packetDataSize, data.length - number * packetDataSize));
            buffer.flip();

            byte[] newData = new byte[buffer.remaining()];
            buffer.get(newData);
            DatagramPacket packet = new DatagramPacket(newData, newData.length, address, port);
            sendPacket(packet);
        }
    }

    public void sendData(byte[] data) throws NetworkException {
        sendData(data, this.serverAddress, this.serverPort);
    }

    private byte[] combineMessage(SortedMap<Integer, byte[]> packets) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte[] packetData : packets.values()) {
            try {
                baos.write(packetData);
            } catch (IOException e) {
            }
        }
        return baos.toByteArray();
    }
}
