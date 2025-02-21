package lab5.collection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lab5.collection.exceptions.ValidationException;
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
    public void add(Flat flat) throws ValidationException {
        if (flat.getId() == null) {
            try {
                Integer id = this.idGenerator.getNextId();
                while (!isIdFree(id)){
                    id = this.idGenerator.getNextId();
                }
                flat.setId(id);
            } catch (ValidationException e) {
                throw new ValidationException("Ошибка в установке id");
            }
        }
        else if(!isIdFree(flat.getId())) {
            try {
                Integer id = this.idGenerator.getNextId();
                while (!isIdFree(id)){
                    id = this.idGenerator.getNextId();
                }
                flat.setId(id);
            } catch (ValidationException e) {
                throw new ValidationException("Ошибка в установке id");
            }
        }

        if (flat.getCreationDate() == null) {
            flat.setCreationDate();
        }

        flat.validate();
        flat.getCoordinates().validate();
        flat.getHouse().validate();
        collection.add(flat);
    }

    /**
     * Проверяет свободен ли данный id
     * @param id id
     * @return boolean
     */
    public boolean isIdFree(Integer id){
        for (Flat flat: collection){
            if (flat.getId() == id) return false;
        }
        return true;
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
     * @param filePath Путь до файла
     * @throws IOException
     */
    public void loadCollection(String filePath) throws IOException, ValidationException {
        List<Flat> flatList = new DumpManager(filePath).jsonFileToFlatList();
        for (Flat flat: flatList) {
            this.add(flat);
        }
    }

    /**
     * Сохраняет коллекцию в файл
     * @param filePath Путь до файла
     * @throws IOException
     */
    public void saveCollection(String filePath) throws IOException {
       new DumpManager(filePath).CollectionToJsonFile(this.sort());
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
