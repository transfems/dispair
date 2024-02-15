/*
 (C)2024 sylvxa
 All Rights Reserved
*/
package lgbt.sylvia.dispair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class Configuration {
    public boolean active;
    public String activity;
    public String token;
    public String webhook;
    public String channel;

    public Configuration(
            String token, String webhook, String channel, String activity, boolean active) {
        this.token = token;
        this.webhook = webhook;
        this.channel = channel;
        this.activity = activity;
        this.active = active;
    }

    public static void save(Configuration config) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);

        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path file = Path.of(configDir.toString(), "dispair.json");
        try (FileWriter writer = new FileWriter(file.toFile())) {
            writer.write(json);
        } catch (IOException ignored) {
        }
    }

    public static Configuration load() {
        Gson gson = new Gson();
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path file = Path.of(configDir.toString(), "dispair.json");
        if (Files.exists(file)) {
            try {
                return gson.fromJson(Files.readString(file), Configuration.class);
            } catch (IOException ignored) {
            }
        }
        return new Configuration(
                "TOKEN_HERE", "WEBHOOK_HERE", "0000000000000000000", "you play Minecraft!", false);
    }
}
