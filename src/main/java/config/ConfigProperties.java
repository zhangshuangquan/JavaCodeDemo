package config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by zsq on 16/12/9.
 */
public class ConfigProperties {

    private static Map<Object, Object> map;

    /**
     * 读取配置文件
     */
    public void init() {
        Properties properties = new Properties();
        map = new HashMap<>();
        //InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("src/main/java/config.properties");
        //URL url2 = ConfigProperties.class.getResource("src/main/java/config.properties");
        File file = new File("src/main/java/config.properties");
        try {
            InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            Set<Map.Entry<Object, Object>> set = properties.entrySet();
            for (Map.Entry<Object, Object> entry : set) {
                map.put(entry.getKey(), entry.getValue());
                System.out.println(entry.getKey()+":"+new String(entry.getValue().toString().getBytes("ISO-8859-1"), "UTF-8"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ConfigProperties configProperties = new ConfigProperties();
        configProperties.init();
    }
}
