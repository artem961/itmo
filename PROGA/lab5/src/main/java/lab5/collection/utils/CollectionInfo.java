package lab5.collection.utils;

/**
 * Запись информации о коллекции.
 *
 * @param collectionType Тип коллекции.
 * @param size           Количество элементов.
 * @param loadedFrom     Из какого файла загружена..
 */
public record CollectionInfo(String collectionType, int size, String loadedFrom) {
}
