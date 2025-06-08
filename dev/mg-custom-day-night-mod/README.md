# Custom Day/Night Mod

A server-side only Fabric mod for Minecraft **1.21.5** that allows you to configure the **length of day and night**.

GitHub: [https://github.com/mozartsghost1212](https://github.com/mozartsghost1212)

## Features

- Configure day and night length using:
  - `day_multiplier` and `night_multiplier`
  - OR `absolute_day_length` and `absolute_night_length` (in ticks)
- Live config reload via `/customdaynight reload`
- Check current settings via `/customdaynight status`
- No client mod required — works with vanilla clients
- No change to world save data

## Configuration

File: `config/customdaynightmod.properties`

```
# If absolute_*_length > 0, it takes priority over multiplier
day_multiplier=1.0
night_multiplier=1.0
absolute_day_length=0
absolute_night_length=0
```

### Example: 10 min day, 5 min night

```
absolute_day_length=12000
absolute_night_length=6000
```

## Commands

- `/customdaynight reload` → Reloads the config file
- `/customdaynight status` → Displays current settings

## Building

```bash
./gradlew build
```

Result: `build/libs/customdaynightmod-1.0.0.jar`

## License

MIT © 2025 mozartsghost1212