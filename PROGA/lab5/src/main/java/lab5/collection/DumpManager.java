package lab5.collection;

import com.google.gson.*;
import lab5.collection.models.Flat;
import lab5.collection.utils.LocalDateAdapter;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Класс для работы с чтением и записью коллекции.
 */
public class DumpManager {

    /**
     * Читает JSON файл
     *
     * @param filePath Путь до файла
     * @return JsonElement из библиотеки Gson
     * @throws IOException
     */
    public static JsonElement readJson(String filePath) throws IOException {
        if (!(new File(filePath).exists())) {
            if (!(new File(filePath + ".json").exists())) {
                throw new IOException("Не удалось найти файл!");
            } else filePath = filePath + ".json";
        }

        InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath));
        StringBuilder jsonString = new StringBuilder();
        int data;
        try {
            while ((data = reader.read()) != -1) {
                jsonString.append((char) data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл!");
        }
        if (jsonString.isEmpty()) jsonString.append("[]");
        JsonElement jsonElement = JsonParser.parseString(jsonString.toString());

        reader.close();
        return jsonElement;
    }

    /**
     * Сохраняет JSON строку в файл.
     *
     * @param jsonString строка в формате JSON
     * @param filePath   Путь до файла.
     * @throws IOException
     */
    public static void saveJson(String jsonString, String filePath) throws IOException {
        if (!(new File(filePath).exists())) {
            if (!(new File(filePath + ".json").exists())) {
                new File(filePath).createNewFile();
            } else filePath = filePath + ".json";
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        try {
            writer.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось записать в файл!");
        }
        writer.close();
    }

    /**
     * Файл с данными в формате JSON преобразует в список Flat.
     *
     * @param filePath Путь до файла.
     * @return List из элементов класса Flat
     * @throws IOException
     */
    public static List<Flat> jsonFileToFlatList(String filePath) throws IOException {
        JsonElement jsonElement = null;
        List<Flat> flatList = new ArrayList<>();

        try {
            jsonElement = readJson(filePath);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray.asList()) {
                Flat flat = gson.fromJson(element, Flat.class);
                if (flat.getCreationDate() == null) flat.setCreationDate();
                flatList.add(flat);
            }
        } else {
            Flat flat = gson.fromJson(jsonElement, Flat.class);
            if (flat.getCreationDate() == null) flat.setCreationDate();
            flatList.add(flat);
        }

        return flatList;
    }

    /**
     * Сохраняет коллекцию в файл JSON.
     *
     * @param filePath   Путь до файла.
     * @param collection Коллекция.
     * @throws IOException
     */
    public static void CollectionToJsonFile(Collection collection, String filePath) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        try {
            saveJson(gson.toJson(collection), filePath);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
