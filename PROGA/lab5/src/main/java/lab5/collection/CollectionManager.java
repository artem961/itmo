package lab5.collection;

import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.*;
import lab5.collection.utils.CollectionInfo;
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
    private CollectionInfo collectionInfo = new CollectionInfo(null, 0, null);
    private final IdGenerator idGenerator = new IdGenerator();
    private final HashSet<Flat> collection = new HashSet<>();
    public final BackupManager backupManager = new BackupManager(".collection_backup_file");

    /**
     * Обновляет информацию о коллекции.
     */
    public void updateCollectionInfo() {
        this.collectionInfo = new CollectionInfo(collectionInfo.collectionType(), collection.size(), collectionInfo.loadedFrom());
    }

    /**
     * Добавляет элемент в коллекцию
     *
     * @param flat
     * @param restoreElements Устанавливать id элементам с одинаковым id или нет.
     * @throws ValidationException
     */
    public void add(Flat flat, boolean restoreElements) throws ValidationException {
        if (flat.getId() == null || !isIdFree(flat.getId())) {
            if (flat.getId() == null || restoreElements) {
                try {
                    Integer id = this.idGenerator.getNextId();
                    while (!isIdFree(id)) {
                        id = this.idGenerator.getNextId();
                    }
                    flat.setId(id);
                } catch (ValidationException e) {
                    throw new ValidationException("Ошибка в установке id");
                }
            } else {
                backupManager.addCorruptedElement(flat);
                return;
            }
        }

        if (flat.getCreationDate() == null) {
            flat.setCreationDate();
        }

        flat.validate();
        if (flat.getCoordinates() != null) flat.getCoordinates().validate();
        if (flat.getHouse() != null) flat.getHouse().validate();
        collection.add(flat);
        makeBackup();
    }

    /**
     * Добавляет элемент в коллекцию
     *
     * @param flat
     * @throws ValidationException
     */
    public void add(Flat flat) throws ValidationException {
        if (flat.getId() == null || !isIdFree(flat.getId())) {
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
        if (flat.getCoordinates() != null) flat.getCoordinates().validate();
        if (flat.getHouse() != null) flat.getHouse().validate();
        collection.add(flat);
        makeBackup();
    }

    /**
     * Обновляет элемент коллекции по id.
     *
     * @param flat
     * @param id
     * @throws ValidationException
     */
    public void update(Flat flat, Integer id) throws ValidationException {
        Flat.ValidateId(id);
        if (!isIdFree(id)) {
            removeById(id);
            flat.setId(id);
        } else {
            flat.setId(id);
        }
        add(flat);
        makeBackup();
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
     * @param flat Объект Flat для добавления.
     * @return Содержался ли элемент в коллекции.
     */
    public boolean remove(Flat flat) {
        boolean rezult = collection.remove(flat);
        makeBackup();
        return rezult;
    }

    /**
     * Удаляет элемент коллекции по ID.
     *
     * @param id ID элемента.
     * @return
     */
    public boolean removeById(Integer id) {
        boolean rezult = collection.removeIf(flat -> flat.getId() == id);
        makeBackup();
        return rezult;
    }

    /**
     * Удаляет все элементы коллекции.
     *
     * @return
     */
    public boolean removeAll() {
        collection.clear();
        makeBackup();
        return true;
    }

    /**
     * @return Возвращает отсортированную коллекцию в виде List
     */
    public List<Flat> sort() {
        List<Flat> sortedList = new ArrayList<>(this.collection);
        Collections.sort(sortedList);
        return sortedList;
    }

    /**
     * Загружает коллекцию из файла.
     *
     * @param filePath
     * @return
     * @throws IOException
     * @throws ValidationException
     */
    public boolean loadCollection(String filePath) throws IOException, ValidationException {
        List<Flat> flatList = DumpManager.jsonFileToFlatList(filePath);
        for (Flat flat : flatList) {
            this.add(flat, false);
        }

        this.collectionInfo = new CollectionInfo(collection.getClass().getName(), collection.size(), filePath);
        if (backupManager.haveCorruptedElements()) return false;
        else return true;
    }

    /**
     * Загрузить повреждённые элементы в коллекцию.
     * @throws ValidationException
     */
    public void loadCorruptedElements() throws ValidationException {
        for (Flat flat : backupManager.getCorruptedElements()) {
            this.add(flat, true);
        }
        backupManager.clearCorruptedElements();
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

    /**
     * Gjkexbnm информацию о коллекции.
     *
     * @return
     */
    public CollectionInfo getCollectionInfo() {
        return collectionInfo;
    }

    /**
     * Сделать бэкап коллекции.
     */
    public void makeBackup() {
        try {
            backupManager.deleteBackupFile();
            saveCollection(backupManager.getBackupFile());
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать бэкап!");
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
