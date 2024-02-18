/*
 (C)2024 sylvxa
 All Rights Reserved
*/
package lgbt.sylvia.dispair.command;

import java.io.File;
import lgbt.sylvia.dispair.Dispair;
import lgbt.sylvia.dispair.listener.MessageListener;
import lgbt.sylvia.dispair.screen.ConfigScreen;
import lgbt.sylvia.dispair.util.ScreenshotHelper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public class ScreenshotCommand extends ListenerAdapter {
    private static final File screenshotDirectory =
            new File(MinecraftClient.getInstance().runDirectory, "screenshots");

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("screenshot")) return;
        if (!Dispair.config.active) {
            event.reply("Dispair is disabled right now.").queue();
            return;
        }

        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.currentScreen instanceof ConfigScreen) {
            event.reply("They are configuring Dispair, no peeking!").queue();
            return;
        }

        event.deferReply().queue();

        minecraft.execute(
                () -> {
                    File file =
                            new File(screenshotDirectory, Util.getFormattedCurrentTime() + ".png");
                    ScreenshotHelper.write(file);

                    FileUpload fileUpload = FileUpload.fromData(file);
                    event.getHook().sendFiles(fileUpload).queue();
                });

        Member member = event.getInteraction().getMember();
        if (member == null) return;
        String username = member.getEffectiveName();

        if (minecraft.player != null) {
            Text alert =
                    Text.translatable("dispair.screenshot.notification", username)
                            .copy()
                            .setStyle(Style.EMPTY.withColor(Formatting.GREEN));
            MessageListener.lastSentToPlayer = alert;
            minecraft.player.sendMessage(alert);
        }
    }
}
