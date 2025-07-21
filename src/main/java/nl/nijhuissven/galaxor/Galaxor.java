package nl.nijhuissven.galaxor;

import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.galaxor.commands.ReloadCommand;
import nl.nijhuissven.galaxor.configuration.MainConfiguration;
import nl.nijhuissven.galaxor.configuration.TrackerConfiguration;
import nl.nijhuissven.galaxor.database.Database;
import nl.nijhuissven.galaxor.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import co.aikar.commands.PaperCommandManager;

@Accessors(fluent = true)
@Getter
public class Galaxor extends JavaPlugin {

    @Getter
    private static Logger logger;
    private static Galaxor instance;

    private Database database;
    private MainConfiguration configuration;
    private TrackerConfiguration trackerConfiguration;

    @Override
    public void onEnable() {
        Galaxor.logger = getLogger();
        Galaxor.instance = this;

        this.configuration = new MainConfiguration();
        this.trackerConfiguration = new TrackerConfiguration();

        this.database = new Database();

        getServer().getPluginManager().registerEvents(new BlockBreakListener(trackerConfiguration.trackers()), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(trackerConfiguration.trackers()), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(trackerConfiguration.trackers()), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(trackerConfiguration.trackers()), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(trackerConfiguration.trackers()), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(trackerConfiguration.trackers()), this);

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ReloadCommand());
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.disconnect();
        }
        logger.info("Galaxor has been disabled.");
    }

    public static Galaxor instance() {
        return instance;
    }

    public static Database db() {
        return instance().database();
    }
}