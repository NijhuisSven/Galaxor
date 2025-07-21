# Galaxor

Galaxor is a flexible Minecraft tracking plugin that lets you easily log all kinds of server statistics, events, and metrics to InfluxDB (Cloud or local) and visualize them in Grafana. You decide what to track via a simple YAML configuration. No coding required—everything is admin-friendly!

---

## Features
- Track standard Minecraft events (joins, deaths, block breaks, damage, advancements, etc.)
- Fully configurable via `tracker.yml` — add your own events, filters, fields, and tags
- Supports InfluxDB Cloud (2.x) and Grafana dashboards
- Only numeric data is logged (privacy-friendly and scalable)
- One listener per event type, but unlimited trackers possible

---

## Installation
1. **Place Galaxor.jar in your plugins folder**
2. **Make sure Vault and an economy plugin (for money tracking) are installed**
3. **Start your server**
4. **Edit `config.yml` for your InfluxDB Cloud setup:**

```yaml
influxdb:
  url: "https://eu-central-1-1.aws.cloud2.influxdata.com"  # Your InfluxDB Cloud URL
  org: "YOUR_ORG"                                         # Your organization name or ID
  bucket: "galaxor"                                       # Your bucket name
  token: "YOUR_API_TOKEN"                                 # Your InfluxDB API token
  measurement: "galaxor_events"
```

5. **(Re)start your server**

---

## Connecting to Grafana
1. Start Grafana and add InfluxDB Cloud as a datasource:
   - URL: `https://eu-central-1-1.aws.cloud2.influxdata.com`
   - Organization: your org
   - Bucket: your bucket
   - Token: your API token
   - Query language: Flux
2. Create a dashboard and use Flux queries like:
   ```flux
   from(bucket: "galaxor")
     |> range(start: -24h)
     |> filter(fn: (r) => r._measurement == "galaxor_events" and r._field == "joins")
     |> aggregateWindow(every: 1h, fn: sum)
     |> yield(name: "sum")
   ```

---

## Adding your own trackers (for admins)

### 1. Open `src/main/resources/tracker.yml`

### 2. Add a new tracker, for example:
```yaml
- name: gold_block_break
  event: BlockBreakEvent
  measurement: galaxor_events
  filter:
    block.type: GOLD_BLOCK
  fields:
    gold_block_broken: 1
  tags:
    world: block.world.name
```
- **name**: Unique name for this tracker
- **event**: Bukkit event class (like `BlockBreakEvent`, `PlayerJoinEvent`, etc.)
- **filter**: (optional) Only log if these conditions are met
- **fields**: Which numeric value to log (can be a property path like `damage` or always `1`)
- **tags**: (optional) Short extra info for filtering/grouping in Grafana

### 3. Reload the config
Use `/galaxor reload` in-game or in the console to apply your changes instantly.

### 4. View your data in Grafana!

---

## Example trackers
```yaml
- name: entity_damage
  event: EntityDamageByEntityEvent
  measurement: galaxor_events
  fields:
    damage: damage
  tags:
    damager: damager.type
    entity: entity.type

- name: player_death
  event: PlayerDeathEvent
  measurement: galaxor_events
  fields:
    deaths: 1
  tags:
    world: player.world.name
```

---

## Frequently Asked Questions

**Q: Can I log anything I want?**  
A: Yes, as long as it's a numeric value (number or property of the event). You can use any Bukkit event and filter on properties.

**Q: Can I group by world, block type, etc.?**  
A: Yes, use tags like `world: block.world.name` or `block: block.type`.

**Q: Can I track money?**  
A: Yes, if you have Vault and an economy plugin, you can log balances periodically or on transactions.

**Q: Do I need to restart the plugin after editing tracker.yml?**  
A: No, use `/galaxor reload` to reload live.

**Q: Can I run InfluxDB locally?**  
A: Yes, but InfluxDB Cloud is recommended for production.

---

## Need help?
- Check the comments in tracker.yml for examples
- Check the logs for warnings about non-numeric fields/tags
- For questions: open an issue on GitHub or ask your server admin

Enjoy using Galaxor! 