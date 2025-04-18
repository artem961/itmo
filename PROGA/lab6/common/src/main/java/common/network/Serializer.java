package common.network;

import java.io.*;
import java.util.Arrays;

public class Serializer {
    public static byte[] serializeObject(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            objectOutputStream.close();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сереализации объекта!");
        }
    }

    public static Object deserialazeObject(byte[] data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();
            objectInputStream.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка десериализации объекта!");
        }
    }
}
