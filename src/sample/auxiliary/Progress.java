package sample.auxiliary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Progress {

    public static Progress instance;
    private Properties properties;
    private Progress() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("resources/progress.properties");
            properties.load(fis);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Progress getInstance() {
        if(instance == null) {
            instance = new Progress();
        }
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public void store() {
        try{
            FileOutputStream fos = new FileOutputStream("resources/progress.properties");
            properties.store(fos, null);
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
