package top.thesumst.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("配置文件 application.properties 未找到");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("加载配置文件失败", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static String getOptional(String key) {
        return props.getProperty(key, "");
    }
}
