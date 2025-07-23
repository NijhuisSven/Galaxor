package nl.nijhuissven.galaxor.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;

@UtilityClass
public class ReflectionUtil {

    public Object getProperty(Object obj, String path) {
        String[] parts = path.split("\\.");
        Object current = obj;
        for (String part : parts) {
            if (current == null) return null;
            try {
                String getter = "get" + Character.toUpperCase(part.charAt(0)) + part.substring(1);
                Method method = current.getClass().getMethod(getter);
                current = method.invoke(current);
            } catch (Exception e) {
                return null;
            }
        }
        return current;
    }
} 