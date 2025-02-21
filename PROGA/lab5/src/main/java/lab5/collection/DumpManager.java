package lab5.collection;

import com.google.gson.*;
import lab5.collection.models.Flat;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Класс для чтения коллекции из файла и записи в файл.
 */
public class DumpManager {
    private final String filePath;

    public DumpManager(String filePath) throws IOException {
        if (!(new File(filePath).exists())) {
            throw new IOException("Файл не найден!");
        }

        this.filePath = filePath;
    }

    public JsonElement readJson() throws IOException {
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
        JsonElement jsonElement = JsonParser.parseString(jsonString.toString());

        reader.close();
        return jsonElement;
    }

    public void saveJson(String jsonString) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        try {
            writer.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось записать в файл!");
        }
        writer.close();
    }

    public List<Flat> jsonFileToFlatList() throws IOException {
        JsonElement jsonElement = null;
        List<Flat> flatList = new ArrayList<>();

        try {
            jsonElement = this.readJson();
        } catch (IOException e) {
            throw new IOException("Не удалось прочитать файл!");
        }

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray.asList()) {
                Flat flat = new Gson().fromJson(element, Flat.class);
                if (flat.getCreationDate() == null) flat.setCreationDate();
                flatList.add(flat);
            }
        } else {
            Flat flat = new Gson().fromJson(jsonElement, Flat.class);
            if (flat.getCreationDate() == null) flat.setCreationDate();
            flatList.add(flat);
        }

        return flatList;
    }

    public void CollectionToJsonFile(Collection collection) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            saveJson(gson.toJson(collection));
        } catch (IOException e) {
            throw new IOException("Не удалось сохранить в файл!");
        }
    }
}
