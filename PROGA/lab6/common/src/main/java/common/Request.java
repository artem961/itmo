package common;

import common.collection.models.Flat;

import java.io.Serializable;

public record Request(String commandName, byte[] data) implements Serializable {
}
