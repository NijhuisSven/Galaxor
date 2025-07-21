package nl.nijhuissven.galaxor.configuration;

import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.galaxor.Galaxor;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public class MainConfiguration {

    private int intervalDelay;

    private String influxMeasurement;
    private String influxUrl;
    private String influxOrg;
    private String influxBucket;
    private String influxToken;

    private final File configFile;
    private final YamlConfigurationLoader loader;

    public MainConfiguration() {
        this.configFile = new File(Galaxor.instance().getDataFolder(), "config.yml");
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

            this.intervalDelay = rootNode.node("data", "interval").getInt(60);

            this.influxUrl = rootNode.node("influxdb", "url").getString("");
            this.influxOrg = rootNode.node("influxdb", "org").getString("");
            this.influxBucket = rootNode.node("influxdb", "bucket").getString("");
            this.influxToken = rootNode.node("influxdb", "token").getString("");
            this.influxMeasurement = rootNode.node("influxdb", "measurement").getString("player_events");

            loader.save(rootNode);
        } catch (IOException e) {
            Galaxor.logger().severe("Could not load configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
}

