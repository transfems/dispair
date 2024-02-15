package lgbt.sylvia.dispair;

import lgbt.sylvia.dispair.command.PlayerCommand;
import lgbt.sylvia.dispair.command.ScreenshotCommand;
import lgbt.sylvia.dispair.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dispair implements ClientModInitializer {
    public static Configuration config = Configuration.load();
    public static Logger LOGGER = LoggerFactory.getLogger("dispair");

    public static JDA jda;

    @Override
    public void onInitializeClient() {
        if (config.activity.isEmpty()) config.activity = "you play minecraft.";
        try {
            jda = JDABuilder.createLight(config.token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).setActivity(Activity.watching(config.activity)).build();
            jda.addEventListener(new MessageListener());

            jda.addEventListener(new PlayerCommand());
            jda.addEventListener(new ScreenshotCommand());

            jda.updateCommands().addCommands(
                    Commands.slash("player", "Get info about the current player."),
                    Commands.slash("screenshot", "Take a screenshot.")
            ).queue();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> Configuration.save(config)));
        } catch (InvalidTokenException e) {
            LOGGER.warn("Token was not provided, inactive.");
        }
    }
}
