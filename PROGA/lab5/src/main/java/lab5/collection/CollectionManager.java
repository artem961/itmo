package lab5.collection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lab5.collection.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Менеджер для управления коллекцией.
 */
public class CollectionManager {
    private final IdGenerator idGenerator = new IdGenerator();
    private final HashSet<Flat> collection = new HashSet<>();

    /**
     * Добавляет элемент в коллекцию
     *
     * @param flat Объект класса Flat
     */
    public void add(Flat flat) {
        if (flat.getId() == null) {
            flat.setId(this.idGenerator.getNextId());
        }
        collection.add(flat);
    }

    /**
     * Добавляет элемент в коллекцию
     *
     * @param jsonObject JsonObject из библиотеки gson
     */
    private void add(JsonObject jsonObject) {
        String name = jsonObject.get("name").toString();
        Coordinates coordinates = new Coordinates(
                (Float) jsonObject.get("coordinates").getAsJsonObject().get("x").getAsFloat(),
                jsonObject.get("coordinates").getAsJsonObject().get("y").getAsDouble());
        float area = jsonObject.get("area").getAsFloat();
        int numberOfRooms = jsonObject.get("numberOfRooms").getAsInt();
        long height = jsonObject.get("height").getAsLong();
        Furnish furnish = Furnish.valueOf("BAD");
        String st = jsonObject.get("transport").toString();
        Transport transport = Transport.valueOf(st);
        System.out.println(transport);
        //House house = new House(
          //      jsonObject.get("house").getAsJsonObject().get("name").toString(),
            //    jsonObject.get("house").getAsJsonObject().get("year").getAsInt(),
              //  jsonObject.get("house").getAsJsonObject().get("numberOfFlatsOnFloor").getAsLong());


        //Flat flat = new Flat(name, coordinates, area, numberOfRooms, height, furnish, transport, house);
        //System.out.println(flat);
    }

    /**
     * Удаляет элемент из коллекции
     *
     * @param flat Объект Flat для добавления
     */
    public void remove(Flat flat) {
        collection.remove(flat);
    }

    /**
     * @return Возвращает отсортированную коллекцию в виде List
     */
    public List sort() {
        List<Flat> sortedList = new ArrayList<>(this.collection);
        Collections.sort(sortedList);
        return sortedList;
    }

    /**
     * Загружает элементы коллекции из файла в формате JSON
     *
     * @param filePath Пусть до файла
     * @throws IOException
     */
    public void loadCollection(String filePath) throws IOException {
        JsonElement jsonElement = new DumpManager(filePath).readJson();

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray.asList()) {
                //this.add(element.getAsJsonObject());
            }
        } else {
            this.add(jsonElement.getAsJsonObject());
        }
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) {
            return "Коллекция пустая";
        }
        StringBuilder string = new StringBuilder();
        for (Flat flat : collection) {
            string.append(flat.toString() + "\n");
        }
        return string.toString().trim();
    }
}
