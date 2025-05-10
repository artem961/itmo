package common.network.enums;

public enum Constants {
    PACKET_SIZE(1024);

    private final int value;
    Constants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
