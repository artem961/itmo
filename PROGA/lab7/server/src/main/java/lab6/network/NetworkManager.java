package lab6.network;

import common.network.enums.Constants;
import common.network.MessageAssembler;
import common.network.exceptions.NetworkException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;

public class NetworkManager {
    private final MessageAssembler messageAssembler;
    private final int bufferSize;
    private final int packetDataSize;
    private final ByteBuffer buffer;
    private final DatagramChannel channel;

    public NetworkManager(DatagramChannel outChannel) {
        this.messageAssembler = new MessageAssembler();
        this.bufferSize = Constants.PACKET_SIZE.getValue();
        this.packetDataSize = bufferSize - 8;
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.channel = outChannel;
    }

    private void sendPacket(SocketAddress address, ByteBuffer buffer) throws NetworkException {
        try {
            synchronized (channel) {
                int sendBytes = channel.send(buffer, address);
                if (sendBytes == 0) {
                    throw new NetworkException("Серверный канал не отправил пакет!");
                }
            }
        } catch (IOException e) {
            throw new NetworkException("Не удалось отправить пакет!");
        }
    }

    public synchronized void sendData(byte[] data, SocketAddress address) throws NetworkException {
        int packetsCount = (int) Math.ceil((double) data.length / packetDataSize);
        for (int number = 0; number < packetsCount; number++) {
            buffer.clear();
            buffer.putInt(number + 1);
            buffer.putInt(packetsCount);
            buffer.put(data, number * packetDataSize, Math.min(packetDataSize, data.length - number * packetDataSize));
            buffer.flip();
            sendPacket(address, buffer);
        }
    }

    public synchronized Map<SocketAddress, byte[]> readFromChannel(DatagramChannel channel) throws IOException, NetworkException {
        messageAssembler.clearReceivedMessages();
        while (true) {
            buffer.clear();
            SocketAddress clientAddress = channel.receive(buffer);
            if (clientAddress == null) break;
            buffer.flip();
            messageAssembler.addPacket(clientAddress, buffer);
        }
        return messageAssembler.getReceivedMessages();
    }
}
