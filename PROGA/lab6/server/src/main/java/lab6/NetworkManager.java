package lab6;

import common.network.Constants;
import common.network.NetworkException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;

public class NetworkManager {
    private final int bufferSize = Constants.PACKET_SIZE.getValue();
    private final int packetDataSize = bufferSize - 8;
    private final ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
    private final DatagramChannel serverChannel;
    private final Map<SocketAddress, SortedMap<Integer, byte[]>> receivedPackets = new HashMap<>();
    private final Map<SocketAddress, byte[]> receivedMessages = new HashMap<>();

    public NetworkManager(DatagramChannel serverChannel) throws IOException {
        this.serverChannel = serverChannel;
        serverChannel.configureBlocking(false);
    }

    private void sendBuffer(SocketAddress address) throws NetworkException {
        try {
            int sendBytes = serverChannel.send(buffer, address);
            if (sendBytes == 0) throw new NetworkException("Серверный канал не отправил пакет!");
        } catch (IOException e) {
            throw new NetworkException("Не удалось отправить пакет!");
        }
    }

    public void sendData(byte[] data, SocketAddress address) throws NetworkException {
        int packetsCount = (int) Math.ceil((double) data.length / packetDataSize);
        for (int number = 0; number < packetsCount; number++) {
            buffer.clear();
            buffer.putInt(number + 1);
            buffer.putInt(packetsCount);
            buffer.put(data, number * packetDataSize, Math.min(packetDataSize, data.length - number * packetDataSize));
            buffer.flip();
            sendBuffer(address);
        }
    }

    private byte[] combineMessage(SortedMap<Integer, byte[]> packets) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (byte[] packetData : packets.values()) {
                baos.write(packetData);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public Map<SocketAddress, byte[]> readFromChannel(DatagramChannel channel) throws IOException {
        receivedMessages.clear();
        while (true) {
            buffer.clear();
            SocketAddress clientAddress = channel.receive(buffer);
            if (clientAddress == null) break;
            buffer.flip();
            saveBufferToMap(clientAddress);
        }
        return receivedMessages;
    }

    private void saveBufferToMap(SocketAddress clientAddress) {
        int packetNumber = buffer.getInt();
        int totalCount = buffer.getInt();
        byte[] packetData = new byte[buffer.remaining()];
        buffer.get(packetData);
        receivedPackets.computeIfAbsent(clientAddress, key -> new TreeMap<>())
                .put(packetNumber, packetData);
        if (receivedPackets.get(clientAddress).size() == totalCount){
            receivedMessages.put(clientAddress, combineMessage(receivedPackets.get(clientAddress)));
            receivedPackets.remove(clientAddress);
        }
    }
}
