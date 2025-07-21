package nl.nijhuissven.galaxor.listeners;

import nl.nijhuissven.galaxor.Galaxor;
import nl.nijhuissven.galaxor.configuration.TrackerConfiguration.TrackerEntry;
import nl.nijhuissven.galaxor.util.ReflectionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerQuitListener implements Listener {
    private final List<TrackerEntry> quitTrackers;

    public PlayerQuitListener(List<TrackerEntry> allTrackers) {
        this.quitTrackers = allTrackers.stream()
                .filter(t -> "PlayerQuitEvent".equals(t.event()))
                .collect(Collectors.toList());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (TrackerEntry tracker : quitTrackers) {
            if (matchesFilter(event, tracker.filter())) {
                Map<String, Object> fields = extractValues(event, tracker.fields(), tracker.name());
                Map<String, String> tags = extractTags(event, tracker.tags(), tracker.name());
                Galaxor.db().writeEvent(tracker.measurement(), fields, tags);
            }
        }
    }

    private boolean matchesFilter(Object event, Map<String, String> filter) {
        if (filter == null || filter.isEmpty()) return true;
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            Object value = ReflectionUtil.getProperty(event, entry.getKey());
            if (value == null || !value.toString().equalsIgnoreCase(entry.getValue())) return false;
        }
        return true;
    }

    private Map<String, Object> extractValues(Object event, Map<String, String> mapping, String trackerName) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
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

    private Map<String, String> extractTags(Object event, Map<String, String> mapping, String trackerName) {
        java.util.Map<String, String> result = new java.util.HashMap<>();
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