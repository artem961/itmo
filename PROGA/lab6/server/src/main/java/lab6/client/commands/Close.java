package lab6.client.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import lab6.Server;
import lab6.collection.CollectionManager;

/**
 * Команда выходит без сохранения коллекции.
 */
public class Close extends Command {
    private final Server server;
    private final CollectionManager collectionManager;

    public Close(Server server, CollectionManager collectionManager) {
        super("close", "Завершает работу сервера без сохранения в файл.");
        this.server = server;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        collectionManager.backupManager.deleteBackupFile();
        server.close();
        return true;
    }
}
