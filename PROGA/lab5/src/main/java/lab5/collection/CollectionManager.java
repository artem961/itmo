package lab5.collection;

import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.*;
import lab5.collection.utils.IdGenerator;

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
                while (!isIdFree(id)) {
                    id = this.idGenerator.getNextId();
                }
                flat.setId(id);
            } catch (ValidationException e) {
                throw new ValidationException("Ошибка в установке id");
            }
        } else if (!isIdFree(flat.getId())) {
            try {
                Integer id = this.idGenerator.getNextId();
                while (!isIdFree(id)) {
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
     *
     * @param id id
     * @return boolean
     */
    public boolean isIdFree(Integer id) {
        for (Flat flat : collection) {
            if (flat.getId() == id) return false;
        }
        return true;
    }

    /**
     * Удаляет элемент из коллекции
     *
     * @param flat Объект Flat для добавления
     * @return Содержался ли элемент в коллекции
     */
    public boolean remove(Flat flat) {
        return collection.remove(flat);
    }

    /**
     * Удаляет элемент коллекции по ID.
     *
     * @param id ID элемента.
     * @return
     */
    public boolean removeById(Integer id) {
        return collection.removeIf(flat -> flat.getId() == id);
    }

    /**
     * Удаляет все элементы коллекции.
     * @return
     */
    public boolean removeAll() {
        collection.clear();
        return true;
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
     * Загружает элементы коллекции из файла.
     *
     * @param filePath Путь до файла
     * @throws IOException
     */
    public void loadCollection(String filePath) throws IOException, ValidationException {
        List<Flat> flatList = DumpManager.jsonFileToFlatList(filePath);
        for (Flat flat : flatList) {
            this.add(flat);
        }
    }

    /**
     * Сохраняет коллекцию в файл.
     *
     * @param filePath Путь до файла
     * @throws IOException
     */
    public void saveCollection(String filePath) throws IOException {
        DumpManager.CollectionToJsonFile(this.sort(), filePath);
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
