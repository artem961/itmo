package lab6.network;

import common.network.enums.Constants;
import common.network.MessageAssembler;
import common.network.exceptions.NetworkException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Map;

public class NetworkManager {
    private final DatagramSocket socket;
    private final int bufferSize;
    private final int packetDataSize;
    private final MessageAssembler messageAssembler;
    private final SocketAddress serverSocket;

    public NetworkManager(int serverPort, InetAddress serverAddress) throws SocketException {
        this.messageAssembler = new MessageAssembler();
        serverSocket = new InetSocketAddress(serverAddress, serverPort);
        bufferSize = Constants.PACKET_SIZE.getValue();
        packetDataSize = bufferSize - 8;

        socket = new DatagramSocket();
        socket.setReceiveBufferSize(bufferSize * 1024);
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
        Map<SocketAddress, byte[]> messages = null;
        while (true) {
            DatagramPacket dp = receivePacket();
            ByteBuffer buffer = ByteBuffer.wrap(dp.getData());
            messageAssembler.addPacket(serverSocket, buffer);
            messages = messageAssembler.getReceivedMessages();
            if (messages.size() != 0) {
                messageAssembler.clearReceivedMessages();
                return messages.get(serverSocket);
            }
        }
    }

    public void sendData(byte[] data, SocketAddress socketAddress) throws NetworkException {
        int packetsCount = (int) Math.ceil((double) data.length / packetDataSize);
        for (int number = 0; number < packetsCount; number++) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            buffer.putInt(number + 1);
            buffer.putInt(packetsCount);
            buffer.put(data, number * packetDataSize, Math.min(packetDataSize, data.length - number * packetDataSize));
            buffer.flip();
            byte[] newData = new byte[buffer.remaining()];
            buffer.get(newData);
            DatagramPacket packet = new DatagramPacket(newData, newData.length, socketAddress);
            sendPacket(packet);
        }
    }

    public void sendData(byte[] data) throws NetworkException {
        sendData(data, serverSocket);
    }
}
