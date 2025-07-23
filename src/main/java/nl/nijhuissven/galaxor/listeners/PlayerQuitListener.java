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

import static nl.nijhuissven.galaxor.util.EventExtractionUtil.*;

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
} 