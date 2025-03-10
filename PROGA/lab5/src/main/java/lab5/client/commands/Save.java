package lab5.client.commands;

import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;

import java.io.IOException;

public class Save extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public Save(Console console, CollectionManager collectionManager) {
        super("save", "Сохраняет коллекцию в файл.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        try {
            collectionManager.saveCollection(args[0]);
            collectionManager.backupManager.deleteBackupFile();
        } catch (IOException e) {
            throw new CommandExecutionError(e.getMessage());
        } catch (IndexOutOfBoundsException e){
            throw new CommandExecutionError("Введите имя файла!");
        }
        console.writeln("Коллекция успешно сохранена в файл " + args[0] + "!");
        return true;
    }
}
