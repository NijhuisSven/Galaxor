package nl.nijhuissven.galaxor.listeners;

import nl.nijhuissven.galaxor.Galaxor;
import nl.nijhuissven.galaxor.configuration.TrackerConfiguration.TrackerEntry;
import nl.nijhuissven.galaxor.util.ReflectionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nl.nijhuissven.galaxor.util.EventExtractionUtil.*;

public class PlayerDeathListener implements Listener {
    private final List<TrackerEntry> deathTrackers;

    public PlayerDeathListener(List<TrackerEntry> allTrackers) {
        this.deathTrackers = allTrackers.stream()
                .filter(t -> "PlayerDeathEvent".equals(t.event()))
                .collect(Collectors.toList());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        for (TrackerEntry tracker : deathTrackers) {
            if (matchesFilter(event, tracker.filter())) {
                Map<String, Object> fields = extractValues(event, tracker.fields(), tracker.name());
                Map<String, String> tags = extractTags(event, tracker.tags(), tracker.name());
                Galaxor.db().writeEvent(tracker.measurement(), fields, tags);
            }
        }
    }
} 