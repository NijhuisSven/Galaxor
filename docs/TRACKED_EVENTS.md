# Overview of all tracked events in Galaxor

Below is an overview of all standard events currently supported by Galaxor and how they are logged. You can directly modify or extend these events via `tracker.yml`.

---

## BlockBreakEvent
- **diamond_ore_break**: Counts every time a diamond ore is broken.
  - **Field:** `diamond_ore_broken: 1`
  - **Tag:** `world: block.world.name`
- **gold_ore_break**: Counts every time a gold ore is broken.
  - **Field:** `gold_ore_broken: 1`
  - **Tag:** `world: block.world.name`
- **stone_break**: Counts every time a stone block is broken.
  - **Field:** `stone_broken: 1`
  - **Tag:** `world: block.world.name`

**Example query (Flux):**
```flux
from(bucket: "galaxor")
  |> range(start: -7d)
  |> filter(fn: (r) => r._measurement == "galaxor_events" and r._field == "diamond_ore_broken")
  |> aggregateWindow(every: 1d, fn: sum)
  |> yield(name: "sum")
```

---

## PlayerJoinEvent
- **player_joins**: Counts every time a player joins.
  - **Field:** `joins: 1`
  - **Tags:** none

---

## PlayerDeathEvent
- **player_death**: Counts every time a player dies.
  - **Field:** `deaths: 1`
  - **Tag:** `world: player.world.name`

---

## PlayerRespawnEvent
- **player_respawn**: Counts every time a player respawns.
  - **Field:** `respawns: 1`
  - **Tag:** `world: player.world.name`

---

## PlayerQuitEvent
- **player_quit**: Counts every time a player leaves the server.
  - **Field:** `quits: 1`
  - **Tags:** none

---

## EntityDamageByEntityEvent
- **entity_damage**: Logs the actual damage value of every hit.
  - **Field:** `damage: damage` (the value from the event)
  - **Tags:** `damager: damager.type`, `entity: entity.type`

**Example query (Flux):**
```flux
from(bucket: "galaxor")
  |> range(start: -24h)
  |> filter(fn: (r) => r._measurement == "galaxor_events" and r._field == "damage")
  |> aggregateWindow(every: 1h, fn: sum)
  |> yield(name: "sum")
```

---

## PlayerAdvancementDoneEvent
- **player_advancement**: Counts every time a player completes an advancement.
  - **Field:** `advancements: 1`
  - **Tag:** `advancement: advancement.key`

---

## Want to add your own?
See `README.md` and `tracker.yml` for instructions on adding your own events, filters, fields, and tags.

---

**Note:**
- Only numeric fields are logged.
- Tags are meant for short, non-sensitive values (such as world, block type, entity type, etc.).
- You can change everything live using `/galaxor reload`. 