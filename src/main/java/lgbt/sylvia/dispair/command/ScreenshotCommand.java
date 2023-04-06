package lgbt.sylvia.dispair.command;

import lgbt.sylvia.dispair.listener.MessageListener;
import lgbt.sylvia.dispair.screen.ConfigScreen;
import lgbt.sylvia.dispair.util.TextureUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import java.io.File;
import java.util.Objects;

public class ScreenshotCommand extends ListenerAdapter {
    private static final File screenshotDirectory = new File(MinecraftClient.getInstance().runDirectory, "screenshots");

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("screenshot")) {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            if (minecraft.currentScreen instanceof ConfigScreen) {
                event.reply("They are configuring Dispair, no peeking!").queue();
                return;
            }
            minecraft.execute(() -> {
                File file = new File(screenshotDirectory, Util.getFormattedCurrentTime() + ".png");
                TextureUtil.saveScreenshot(file);

                FileUpload fileUpload = FileUpload.fromData(file);
                event.replyFiles(fileUpload).queue();
            });

            if (minecraft.player != null) {
                Text alert = Text.of(String.format("§l§a%s §r§atook a screenshot!", Objects.requireNonNull(event.getInteraction().getMember()).getEffectiveName()));
                MessageListener.lastSentToPlayer = alert;
                minecraft.player.sendMessage(alert);
            }
        }
    }
}
