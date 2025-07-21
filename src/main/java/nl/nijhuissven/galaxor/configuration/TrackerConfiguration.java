package nl.nijhuissven.galaxor.configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.galaxor.Galaxor;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Getter
@Accessors(fluent = true)
public class TrackerConfiguration {
    private List<TrackerEntry> trackers = Collections.emptyList();

    private final File configFile;
    private final YamlConfigurationLoader loader;

    public TrackerConfiguration() {
        this.configFile = new File(Galaxor.instance().getDataFolder(), "tracker.yml");
        Path configPath = configFile.toPath();
        this.loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
        loadConfig();
    }

    public void reload() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            CommentedConfigurationNode rootNode = loader.load();
            List<TrackerEntry> loadedTrackers = new ArrayList<>();
            for (CommentedConfigurationNode trackerNode : rootNode.node("trackers").childrenList()) {
                String name = trackerNode.node("name").getString("");
                String event = trackerNode.node("event").getString("");
                String measurement = trackerNode.node("measurement").getString("galaxor_events");
                Map<String, String> filter = parseMap(trackerNode.node("filter"));
                Map<String, String> fields = parseMap(trackerNode.node("fields"));
                Map<String, String> tags = parseMap(trackerNode.node("tags"));
                if (!name.isEmpty() && !event.isEmpty()) {
                    loadedTrackers.add(TrackerEntry.builder()
                        .name(name)
                        .event(event)
                        .measurement(measurement)
                        .filter(filter)
                        .fields(fields)
                        .tags(tags)
                        .build());
                }
            }
            this.trackers = loadedTrackers;
            loader.save(rootNode);
        } catch (IOException e) {
            Galaxor.logger().severe("Could not load tracker configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load tracker configuration", e);
        }
    }

    private Map<String, String> parseMap(CommentedConfigurationNode node) {
        Map<String, String> map = new HashMap<>();
        if (node == null || node.empty()) return map;
        if (node.isMap()) {
            for (Map.Entry<Object, ? extends CommentedConfigurationNode> entry : node.childrenMap().entrySet()) {
                map.put(String.valueOf(entry.getKey()), entry.getValue().getString(""));
            }
        } else if (node.isList()) {
            int i = 0;
            for (CommentedConfigurationNode child : node.childrenList()) {
                String value = child.getString("");
                map.put(String.valueOf(i++), value);
            }
        }
        return map;
    }

    @Getter
    @Builder
    public static class TrackerEntry {
        private final String name;
        private final String event;
        private final String measurement;
        private final Map<String, String> filter;
        private final Map<String, String> fields;
        private final Map<String, String> tags;
    }
}
