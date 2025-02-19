# ☄️ AsteroidRandomizer

AsteroidRandomizer is a spigot plugin designed easily create "space" environment worlds.
The plugin pastes random schematics into a designated region, splitting the area into sub-regions
using a configurable gap value. Schematics are only pasted when an air block location has been found!

## 🪐 Features
- Periodically pastes a random schematic inside a sub-region of the configured world.
- Configurable delay between pastes, allowing for controlled generation speed.
- Region is split into sub-regions based on a "gap" setting, controlling schematic density.
- Supports teleportation to pasted locations via a command and substantial logging.

## 🚀 Configuration
Modify the `settings.yml` file to customize the plugin's behavior:

```yaml
settings:
  world: "DeepSpace" # The world where schematics will be pasted.
  teleport-command: "/teleport %x% %y% %z%" # Command for teleporting to pasted schematics.
  paste:
    delay-seconds: 10 # Time interval (in seconds) between each schematic paste.
    gap: 150 # Defines the sub-region size; smaller values increase density.
  region:
    min-x: -10000
    max-x: 10000
    min-y: 0
    max-y: 220
    min-z: -10000
    max-z: 10000
```

## ⚙️ Installation
1. Create the world where you want to paste schematics. (Recommended to use a void world for the "space" effect)
2. Download the latest release of the plugin.
3. Place the `.jar` file into your Spigot server's `plugins` directory.
4. Restart your server to generate the plugin's configuration.
5. Configure the plugin as needed, by default the paste-task will be "disabled".
6. Save your configuration, restart your server and start the task by using the `/asteroidrandomizer toggle` command.

## 👽 Commands
- `/asteroidrandomizer toggle` - Start/stop the pasting procedure. 
- `/asteroidrandomizer status` - See the status of the paste task.

## Contributing
Pull requests and suggestions are welcome! Currently, the plugin comes as-is, with no plans for major updates.

## License
This project is licensed under the MIT License.
