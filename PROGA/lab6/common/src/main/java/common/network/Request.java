package common.network;

import common.collection.models.Flat;

import java.io.Serializable;

public record Request(String commandName, String[] args, Flat flat) implements Serializable {
}
