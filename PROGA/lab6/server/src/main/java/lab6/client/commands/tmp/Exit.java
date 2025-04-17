package lab6.client.commands;

import common.client.commands.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.CollectionManager;

import static java.lang.System.exit;

/**
 * Команда выходит без сохранения коллекции.
 */
public class Exit extends Command {
    public final CollectionManager collectionManager;

    public Exit(CollectionManager collectionManager) {
        super("exit", "Завершает программу без сохранения в файл.");
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        collectionManager.backupManager.deleteBackupFile();
        exit(0);
        return super.apply(args);
    }
}
