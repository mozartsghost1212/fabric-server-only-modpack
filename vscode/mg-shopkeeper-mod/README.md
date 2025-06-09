# Shopkeeper Mod

A server-side Fabric mod for Minecraft 1.21.5 that allows operators to spawn and manage persistent shopkeeper Villagers in protected shop structures.

## Features

- `/spawnshopkeeper <type>` → Spawns a shop with protected area and a persistent Villager with custom trades.
- `/listshopkeepers` → Lists all shops.
- `/removeshopkeeper <shop_id>` → Removes a shop.
- `/reloadshoptypes` → Reloads `shop_types.json` live.
- `/reloadshopkeepers` → Rebuilds all shops and respawns missing shopkeepers.
- **Block break/place protection** inside shop area.
- **Per-shop size** (configurable protection radius).
- **Shop owner tracking**.

## Permissions

- All commands require OP permission level ≥ 2.

## Installation

1. Build with `./gradlew build`.
2. Place resulting JAR in server `mods/` folder.
3. Create `config/shop_types.json` (example included).

## Config: shop_types.json

```json
{
  "blacksmith": {
    "size": 3,
    "trades": [
      {
        "cost": {"item": "minecraft:emerald", "count": 5},
        "result": {"item": "minecraft:diamond_sword", "count": 1},
        "max_uses": 10
      }
    ]
  }
}
```

- `size`: Protection radius around shop center.
- `trades`: List of trade definitions.

## How Protection Works

- Block breaking and placing is prevented inside the shop protection area.
- Protection is per-shop and configurable.

## Roadmap

- Owner-only remove option.
- Multi-world support.
- Shop expiration system.
- Shop display names.

## License

MIT