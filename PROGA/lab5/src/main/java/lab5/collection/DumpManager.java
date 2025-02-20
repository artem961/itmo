package lab5.collection;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lab5.collection.models.Coordinates;
import lab5.collection.models.Flat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;


/**
 * Класс для чтения коллекции из файла и записи в файл.
 */
public class DumpManager {
    private final InputStreamReader reader;
    private final String filePath;

    public DumpManager(String filePath) throws IOException {
        if (!(new File(filePath).exists())) {
            throw new IOException("Файл не найден!");
        }
        this.reader = new InputStreamReader(new FileInputStream(filePath));
        this.filePath = filePath;
    }

    public JsonElement readJson() {
        StringBuilder jsonString = new StringBuilder();
        int data;
        try {
            while ((data = this.reader.read()) != -1) {
                jsonString.append((char) data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл!");
        }
        JsonElement jsonElement = JsonParser.parseString(jsonString.toString());
        return jsonElement;


    }
}
