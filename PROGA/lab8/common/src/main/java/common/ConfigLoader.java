package common;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class ConfigLoader  {
    private final Properties properties;

    public ConfigLoader(String fileName){
        properties = new Properties();
        try(Reader reader = new FileReader(fileName)){
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String name){
        return properties.getProperty(name);
    }
}
