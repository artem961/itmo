package lab6.collection;

import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import lab6.collection.database.FlatRepository;
import lab6.collection.database.connection.DBManager;
import lab6.collection.utils.CollectionInfo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

/**
 * Менеджер для управления коллекцией.
 */
public class CollectionManager {
    @Getter
    private CollectionInfo collectionInfo;
    private final Set<Flat> collection;
    private final FlatRepository flatRepository;

    public CollectionManager(DBManager dbManager){
        collectionInfo = new CollectionInfo(null, 0);
        collection = Collections.synchronizedSet(new HashSet<>());
        flatRepository = new FlatRepository(dbManager);
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
     * @param flat Квартира
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

    public void add(Collection<Flat> flats) throws ValidationException{
        for (Flat flat: flats) {
            if (flat.getCreationDate() == null) {
                flat.setCreationDate(LocalDate.now());
            }

            flat.validate();
            if (flat.getCoordinates() != null) flat.getCoordinates().validate();
            if (flat.getHouse() != null) flat.getHouse().validate();
        }
        if (flatRepository.insert(flats) != 0) {
            collection.addAll(flats);
            updateCollectionInfo();
        } else {
            throw new RuntimeException("Не удалось добавить квартиры пачкой!");
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
        if (flatRepository.updateById(flat, id) != 0) {
            collection.removeIf(fl -> fl.getId().equals(id));
            flat.setId(id);
            collection.add(flat);
        } else{
            throw new RuntimeException("Не удалось обновить.");
        }
    }

    /**
     * Удаляет элемент из коллекции
     *
     * @param flat Объект Flat для добавления.
     * @return Содержался ли элемент в коллекции.
     */
    public boolean remove(Flat flat) {
        return removeById(flat.getId(), flat.getUserId());
    }

    /**
     * Удаляет элемент коллекции по ID.
     *
     * @param id ID элемента.
     * @return
     */
    public boolean removeById(Integer id, int userId) {
        if (getAsList().stream()
                .noneMatch(flat -> flat.getId().equals(id) && flat.getUserId() == userId))
        {
            return false;
        }
        flatRepository.removeById(id);
        return collection.removeIf(flat -> flat.getId().equals(id));
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
     * Удаляет все элементы этого пользователя.
     * @param userId
     * @return
     */
    public boolean removeAll(int userId) {
        flatRepository.removeAll(userId);
        collection.removeIf(flat -> flat.getUserId() == userId);
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
        return new ArrayList<>(this.collection);
    }

    /**
     * Обновляет состояние коллекции
     */
    public void refreshCollection() {
        this.collection.addAll(flatRepository.selectAll());
        updateCollectionInfo();
    }

    public boolean isIdFree(Integer id){
        return collection.stream().noneMatch(flat -> flat.getId().equals(id));
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
