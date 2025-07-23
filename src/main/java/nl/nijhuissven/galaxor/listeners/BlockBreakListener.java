package nl.nijhuissven.galaxor.listeners;

import nl.nijhuissven.galaxor.Galaxor;
import nl.nijhuissven.galaxor.configuration.TrackerConfiguration.TrackerEntry;
import nl.nijhuissven.galaxor.util.ReflectionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nl.nijhuissven.galaxor.util.EventExtractionUtil.*;

public class BlockBreakListener implements Listener {
    private final List<TrackerEntry> blockBreakTrackers;

    public BlockBreakListener(List<TrackerEntry> allTrackers) {
        this.blockBreakTrackers = allTrackers.stream()
                .filter(t -> "BlockBreakEvent".equals(t.event()))
                .collect(Collectors.toList());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (TrackerEntry tracker : blockBreakTrackers) {
            if (matchesFilter(event, tracker.filter())) {
                Map<String, Object> fields = extractValues(event, tracker.fields(), tracker.name());
                Map<String, String> tags = extractTags(event, tracker.tags(), tracker.name());
                Galaxor.db().writeEvent(tracker.measurement(), fields, tags);
            }
        }
    }
} 