package org.example.сonfigReader;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private ConfigReader(){
    }

    static {
        try {
            String path = "/Users/user/IdeaProjects/JDBS/src/main/resources/bd.properties";
            FileInputStream inputStream = new FileInputStream(path);
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("File not found");
        }
    }

    public static String getValues(String key){
        return properties.getProperty(key).trim();
    }

}
