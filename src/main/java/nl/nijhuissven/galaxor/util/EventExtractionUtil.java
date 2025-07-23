// File: src/main/java/nl/nijhuissven/galaxor/util/EventExtractionUtil.java
package nl.nijhuissven.galaxor.util;

import lombok.experimental.UtilityClass;
import nl.nijhuissven.galaxor.Galaxor;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class EventExtractionUtil {

    public boolean matchesFilter(Object event, Map<String, String> filter) {
        if (filter == null || filter.isEmpty()) return true;
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            Object value = ReflectionUtil.getProperty(event, entry.getKey());
            if (value == null || !value.toString().equalsIgnoreCase(entry.getValue())) return false;
        }
        return true;
    }

    public Map<String, Object> extractValues(Object event, Map<String, String> mapping, String trackerName) {
        Map<String, Object> result = new HashMap<>();
        if (mapping == null) return result;
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            Object value;
            try {
                value = Integer.valueOf(entry.getValue());
            } catch (NumberFormatException e1) {
                try {
                    value = Double.valueOf(entry.getValue());
                } catch (NumberFormatException e2) {
                    value = ReflectionUtil.getProperty(event, entry.getValue());
                }
            }
            if (value instanceof Number) {
                result.put(entry.getKey(), value);
            } else if (value != null) {
                Galaxor.logger().warning("Skipped non-numeric field '" + entry.getKey() + "' for tracker '" + trackerName + "'");
            }
        }
        return result;
    }

    public Map<String, String> extractTags(Object event, Map<String, String> mapping, String trackerName) {
        Map<String, String> result = new HashMap<>();
        if (mapping == null) return result;
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            Object value = ReflectionUtil.getProperty(event, entry.getValue());
            if (value != null) {
                String str = value.toString();
                if (str.length() <= 32) {
                    result.put(entry.getKey(), str);
                } else {
                    Galaxor.logger().warning("Skipped long tag '" + entry.getKey() + "' for tracker '" + trackerName + "'");
                }
            }
        }
        return result;
    }
}