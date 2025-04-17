package common.client.console;

import java.util.ArrayList;
import java.util.List;

public class BufferedConsole extends StandartConsole {
    List<String> buffer = new ArrayList<>(64);
    boolean bufferMode = false;

    public void setBufferMode(boolean bufferMode) {
        this.bufferMode = bufferMode;
    }

    public boolean isBufferMode() {
        return bufferMode;
    }

    @Override
    public void write(String data) {
        if (bufferMode) buffer.add(data);
        else super.write(data);
    }

    public List<String> getBuffer() {
        return new ArrayList<>(buffer);
    }

    public void clearBuffer() {
        buffer.clear();
    }

    public void flushBuffer() {
        for (String str : buffer) {
            super.write(str);
        }
    }
}
