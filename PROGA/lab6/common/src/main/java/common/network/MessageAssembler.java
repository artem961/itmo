package common.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MessageAssembler {
    private final Map<SocketAddress, SortedMap<Integer, byte[]>> receivedPackets;
    private final Map<SocketAddress, byte[]> receivedMessages;

    public MessageAssembler() {
        this.receivedPackets = new HashMap<>();
        this.receivedMessages = new HashMap<>();
    }

    public void addPacket(SocketAddress clientAddress, ByteBuffer buffer) throws NetworkException {
        if (buffer.remaining() < 8) {
            throw new NetworkException("Слишком маленький размер пакета!");
        }
        int packetNumber = buffer.getInt();
        int totalCount = buffer.getInt();

        byte[] packetData = new byte[buffer.remaining()];
        buffer.get(packetData);

        receivedPackets.computeIfAbsent(clientAddress, key -> new TreeMap<>())
                .put(packetNumber, packetData);

        checkAndCombine(clientAddress, totalCount);
    }

    private void checkAndCombine(SocketAddress clientAddress, int totalCount){
        SortedMap<Integer, byte[]> clientPackets = receivedPackets.get(clientAddress);
        if (clientPackets.size() == totalCount) {
            receivedMessages.put(clientAddress, combineMessage(clientPackets));
            receivedPackets.remove(clientAddress);
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

    public Map<SocketAddress, byte[]> getReceivedMessages() {
        return Map.copyOf(this.receivedMessages);
    }

    public void clearReceivedMessages() {
        this.receivedMessages.clear();
    }
}
