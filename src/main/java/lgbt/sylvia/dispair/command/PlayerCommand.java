/*
 (C)2024 sylvxa
 All Rights Reserved
*/
package lgbt.sylvia.dispair.command;

import java.awt.*;
import lgbt.sylvia.dispair.Dispair;
import lgbt.sylvia.dispair.listener.MessageListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class PlayerCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("player")) return;
        if (!Dispair.config.active) {
            event.reply("Dispair is disabled right now.").queue();
            return;
        }

        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraft.player;

        if (player == null) {
            event.reply("They aren't playing at the moment.").queue();
            return;
        }

        MinecraftServer server = minecraft.getServer();
        if (server == null) {
            event.reply("They aren't in a world, which doesn't make sense but it can happen.")
                    .queue();
            return;
        }

        BlockPos position = player.getBlockPos();

        String healthDisplay = String.format("%s/%s", player.getHealth(), player.getMaxHealth());
        String hungerDisplay = String.format("%s/%s", player.getHungerManager().getFoodLevel(), 20);
        String positionDisplay =
                String.format("%s, %s, %s", position.getX(), position.getY(), position.getZ());
        String worldDisplay;
        if (server.isSingleplayer()) {
            worldDisplay = server.getSaveProperties().getLevelName();
        } else {
            worldDisplay = server.getServerIp();
        }

        MessageEmbed embed =
                new EmbedBuilder()
                        .setTitle(player.getDisplayName().getString())
                        .addField("❤️ Health", healthDisplay, true)
                        .addField("\uD83E\uDD55 Hunger", hungerDisplay, true)
                        .addField("\uD83D\uDCCD Position", positionDisplay, true)
                        .addField(
                                "\uD83C\uDF00 Dimension",
                                player.clientWorld.getDimension().effects().getPath(),
                                true)
                        .addField(
                                "⏰ Day",
                                String.valueOf(player.clientWorld.getTimeOfDay() / 24000L),
                                true)
                        .addField("\uD83C\uDF0E World", worldDisplay, true)
                        .setColor(Color.PINK)
                        .setThumbnail("https://minotar.net/helm/" + player.getName().getString())
                        .build();
        event.replyEmbeds(embed).queue();

        Member member = event.getInteraction().getMember();
        if (member == null) return;
        String username = member.getEffectiveName();

        Text notification =
                Text.translatable("dispair.player.notification", username)
                        .copy()
                        .setStyle(Style.EMPTY.withColor(Formatting.GREEN));
        MessageListener.lastSentToPlayer = notification;
        player.sendMessage(notification);
    }
}
