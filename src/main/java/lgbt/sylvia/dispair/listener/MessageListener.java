package lgbt.sylvia.dispair.listener;

import lgbt.sylvia.dispair.Dispair;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MessageListener extends ListenerAdapter {
    public static Text lastSentToPlayer = Text.of("");
    public static String lastSentToDiscord = "";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        if (minecraft.player == null) return;
        if (event.getAuthor().isBot()) return;

        if (lastSentToDiscord.equals(event.getMessage().getContentDisplay())) return;
        if (!event.getChannel().getId().equals(Dispair.config.channel)) return;

        Text formatted = formatDiscordMessage(event);
        lastSentToPlayer = formatted;
        minecraft.player.sendMessage(formatted);
    }

    private Text formatDiscordMessage(MessageReceivedEvent event) {
        Member author = event.getMember();
        if (author == null) return Text.of("Something went wrong!");

        Color color = author.getColor();
        if (color == null) color = Color.WHITE;

        Style style = Style.EMPTY.withColor(color.getRGB());
        Message message = event.getMessage();

        String authorName = author.getEffectiveName();
        String contentString = message.getContentDisplay();

        String body = String.format("§l<%s> §r§f%s", authorName, contentString);
        Text finished = Text.of(body).copy().setStyle(style);

        for (Message.Attachment attachment : event.getMessage().getAttachments()) {
            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, attachment.getUrl());
            String display = String.format(" §n§9<%s>", attachment.getFileName());
            Text attachmentMessage = Text.of(display).copy().setStyle(Style.EMPTY.withClickEvent(clickEvent));
            finished = finished.copy().append(attachmentMessage);
        }

        return finished;
    }
}
