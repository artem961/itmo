package lab6.commands.server;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import lab6.collection.CollectionManager;

import java.io.IOException;

/**
 * Команда сохраняет коллекцию в файл.
 */
public class Save extends Command {
    private final CollectionManager collectionManager;

    public Save(CollectionManager collectionManager) {
        super("save", "Сохраняет коллекцию в файл.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        try {
            collectionManager.saveCollection(args[0]);
            collectionManager.backupManager.deleteBackupFile();
        } catch (IOException e) {
            throw new CommandExecutionError(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            throw new CommandExecutionError("Введите имя файла!");
        }
        return Response.builder()
                .setMessage("Коллекция успешно сохранена в файл " + args[0] + "!")
                .build();
    }
}
