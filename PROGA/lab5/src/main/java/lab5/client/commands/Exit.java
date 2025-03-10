package lab5.client.commands;

import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;

import static java.lang.System.exit;

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
