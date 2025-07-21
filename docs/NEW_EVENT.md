# Adding a new event in code (for developers)

Do you want to add a new Minecraft event to Galaxor as a developer, so that admins can track it via tracker.yml? Follow these steps:

---

## 1. Create a new Listener class

For example, for a custom event `PlayerFishEvent`:

```java
package nl.nijhuissven.galaxor.listeners;

import nl.nijhuissven.galaxor.Galaxor;
import nl.nijhuissven.galaxor.configuration.TrackerConfiguration.TrackerEntry;
import nl.nijhuissven.galaxor.util.ReflectionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerFishListener implements Listener {
    private final List<TrackerEntry> fishTrackers;

    public PlayerFishListener(List<TrackerEntry> allTrackers) {
        this.fishTrackers = allTrackers.stream()
                .filter(t -> "PlayerFishEvent".equals(t.event()))
                .collect(Collectors.toList());
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        for (TrackerEntry tracker : fishTrackers) {
            if (matchesFilter(event, tracker.filter())) {
                Map<String, Object> fields = extractValues(event, tracker.fields(), tracker.name());
                Map<String, String> tags = extractTags(event, tracker.tags(), tracker.name());
                Galaxor.db().writeEvent(tracker.measurement(), fields, tags);
            }
        }
    }

    // Use the same matchesFilter, extractValues, extractTags as in other listeners
}
```

---

## 2. Register the listener in Galaxor.java

Add in `onEnable()`:
```java
getServer().getPluginManager().registerEvents(new PlayerFishListener(trackerConfiguration.trackers()), this);
```

---

## 3. Add a tracker in tracker.yml

```yaml
- name: player_fish
  event: PlayerFishEvent
  measurement: galaxor_events
  fields:
    fish: 1
  tags:
    world: player.world.name
```

---

## 4. Reload the config
Use `/galaxor reload` to activate your new event immediately.

---

## **Tips**
- Always use numeric fields (numbers or property paths that resolve to a number).
- Use short, non-sensitive tags for extra filtering in Grafana.
- Check the logs for any warnings about non-numeric fields/tags.

---

**This is how you easily add a new event to Galaxor as a developer!** 