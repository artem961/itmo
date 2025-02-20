package lab5;

import lab5.collection.CollectionManager;
import lab5.collection.DumpManager;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.Coordinates;
import lab5.collection.models.Flat;
import lab5.collection.models.House;
import lab5.collection.models.Transport;

import java.io.*;

public class Main {
    public static void main(String... args){

        try {
            CollectionManager collectionManager = new CollectionManager();
            collectionManager.loadCollection("test.json");
            System.out.println(collectionManager.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

    }
}
