package lab6.collection;

import common.collection.models.Flat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Менеджер бэкапа.
 */
public class BackupManager {
    private final String backupFile;
    private final HashSet<Flat> corruptedElements = new HashSet<>();

    public BackupManager(String backupFile) {
        this.backupFile = backupFile;
    }

    /**
     * Проверить существование бэкап файла.
     *
     * @return
     */
    public boolean checkBackupFile() {
        return new File(backupFile).exists();
    }

    /**
     * Получить имя бэкап файла.
     *
     * @return
     */
    public String getBackupFile() {
        return backupFile;
    }

    /**
     * Удалить бэкап файл.
     */
    public void deleteBackupFile() {
        if (checkBackupFile()) {
            new File(backupFile).delete();
        }
    }

    /**
     * Добавляет повреждённый элемент.
     *
     * @param flat
     */
    public void addCorruptedElement(Flat flat) {
        this.corruptedElements.add(flat);
    }

    /**
     * Возвращает true если есть повреждённые элементы.
     *
     * @return
     */
    public boolean haveCorruptedElements() {
        return corruptedElements.size() != 0;
    }

    /**
     * Удалить повреждённые элементы.
     */
    public void clearCorruptedElements() {
        corruptedElements.clear();
    }

    /**
     * Получить список повреждённых элементов.
     *
     * @return
     */
    public List<Flat> getCorruptedElements() {
        return new ArrayList<>(corruptedElements);
    }
}
