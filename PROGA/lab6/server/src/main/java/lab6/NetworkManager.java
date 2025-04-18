package lab6;

import common.network.Constants;
import common.network.NetworkException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public class NetworkManager {
    private final DatagramSocket socket;
    private final int port;
    private final int bufferSize = Constants.PACKET_SIZE.getValue();
    private final int packetDataSize = bufferSize - 8;

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

    private DatagramPacket receivePacket() throws NetworkException {
        try {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            socket.receive(dp);
            return dp;
        } catch (IOException e) {
            throw new NetworkException(e);
        }
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

    public UserDataObject receiveData() throws NetworkException {
        SortedMap<Integer, byte[]> packets = new TreeMap<>();
        int totalCount = -1;
        InetAddress userAddress = null;
        int userPort = 0;

        while (true) {
            DatagramPacket dp = receivePacket();
            byte[] packetData = dp.getData();
            ByteBuffer buffer = ByteBuffer.wrap(packetData, 0, 8);
            int packetNumber = buffer.getInt();
            int count = buffer.getInt();
            if (totalCount == -1){
                totalCount = count;
                userAddress = dp.getAddress();
                userPort = dp.getPort();
            }

            packetData = Arrays.copyOfRange(packetData, 8, dp.getLength());
            packets.put(packetNumber, packetData);
            if (packetNumber == totalCount) break;
        }
        return new UserDataObject(userAddress, userPort, combineMessage(packets));
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
