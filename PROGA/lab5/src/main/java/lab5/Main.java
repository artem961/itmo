package lab5;

import lab5.collection.CollectionManager;
import lab5.collection.DumpManager;
import lab5.collection.models.Coordinates;
import lab5.collection.models.Flat;
import lab5.collection.models.House;
import lab5.collection.models.Transport;

import java.io.*;

public class Main {
    public static void main(String... args){
        /*
        Flat flat1 = new Flat("flat1", new Coordinates(Float.valueOf(1.2f), 2), 2f, 2, 52l, Transport.LITTLE, new House("house", 1222, Long.valueOf(12l)));
        Flat flat2 = new Flat("flat2", new Coordinates(Float.valueOf(1.2f), 2), 2f, 2, 52l, Transport.LITTLE, new House("house", 1222, Long.valueOf(12l)));
        CollectionManager collectionManager = new CollectionManager();
        collectionManager.add(flat2);
        collectionManager.add(flat1);
        System.out.println(collectionManager.toString());

        System.out.println(collectionManager.sort());

        */

        Coordinates coordinates = new Coordinates(-2000f, 2.2d);
        /*
        try {
            CollectionManager collectionManager = new CollectionManager();
            //collectionManager.loadCollection("test.json");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

         */
    }
}
