package lab6.collection;



import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import lab6.collection.database.DBQueryManager;
import lab6.collection.database.FlatRepository;
import lab6.collection.utils.CollectionInfo;

import java.time.LocalDate;
import java.util.*;

/**
 * Менеджер для управления коллекцией.
 */
public class CollectionManager {
    private CollectionInfo collectionInfo;
    private final HashSet<Flat> collection;
    private final FlatRepository flatRepository;

    public CollectionManager(){
        collectionInfo = new CollectionInfo(null, 0);
        collection = new HashSet<>();
        flatRepository = new FlatRepository();
        refreshCollection();
    }

    /**
     * Обновляет информацию о коллекции.
     */
    public void updateCollectionInfo() {
        this.collectionInfo = new CollectionInfo(collection.getClass().getName(), collection.size());
    }

    /**
     * Добавляет элемент в коллекцию
     *
     * @param flat
     * @throws ValidationException
     */
    public void add(Flat flat) throws ValidationException {
        if (flat.getCreationDate() == null) {
            flat.setCreationDate(LocalDate.now());
        }

        flat.validate();
        if (flat.getCoordinates() != null) flat.getCoordinates().validate();
        if (flat.getHouse() != null) flat.getHouse().validate();

        if (flatRepository.insert(flat) != 0) {
            collection.add(flat);
            updateCollectionInfo();
        } else {
            throw new RuntimeException("Не удалось добавить квартиру!");
        }
    }

    /**
     * Обновляет элемент коллекции по id.
     *
     * @param flat
     * @param id
     * @throws ValidationException
     */
    public void update(Flat flat, Integer id) throws ValidationException {
        if (isIdFree(id)){
            throw new RuntimeException("Элемента с таким id не существует!");
        }
        if (flatRepository.updateById(flat, id) != 0) {
            collection.removeIf(fl -> fl.getId().equals(id));
            flat.setId(id);
            collection.add(flat);
        } else{
            throw new RuntimeException("Не удалось обновить flat");
        }
    }

    /**
     * Удаляет элемент из коллекции
     *
     * @param flat Объект Flat для добавления.
     * @return Содержался ли элемент в коллекции.
     */
    public boolean remove(Flat flat) {
        boolean rezult = removeById(flat.getId());
        return rezult;
    }

    /**
     * Удаляет элемент коллекции по ID.
     *
     * @param id ID элемента.
     * @return
     */
    public boolean removeById(Integer id) {
        flatRepository.removeById(id);
        boolean rezult = collection.removeIf(flat -> flat.getId().equals(id));
        return rezult;
    }

    /**
     * Удаляет все элементы коллекции.
     *
     * @return
     */
    public boolean removeAll() {
        flatRepository.removeAll();
        collection.clear();
        return true;
    }

    /**
     * @return Возвращает отсортированную коллекцию в виде List
     */
    public List<Flat> sort() {
        List<Flat> sortedList = getAsList();
        Collections.sort(sortedList);
        return sortedList;
    }

    /**
     * @return Возвращает отсортированную коллекцию в виде List
     */
    public List<Flat> getAsList() {
        List<Flat> list = new ArrayList<>(this.collection);
        return list;
    }

    /**
     * Обновляет состояние коллекции
     *
     * @return
     */
    public void refreshCollection() {
        this.collection.addAll(flatRepository.selectAll());
        updateCollectionInfo();
    }

    /**
     * Gjkexbnm информацию о коллекции.
     * @return
     */
    public CollectionInfo getCollectionInfo() {
        return collectionInfo;
    }

    public boolean isIdFree(Integer id){
        return !collection.stream().anyMatch(flat -> flat.getId().equals(id));
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
