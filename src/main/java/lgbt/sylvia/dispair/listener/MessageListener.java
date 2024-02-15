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
import net.minecraft.util.Formatting;
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

    private Text formatBodyComponent(Member author, Message message) {
        Color color = author.getColor();
        if (color == null) color = Color.WHITE;

        String authorName = author.getEffectiveName();
        String bodyString = message.getContentDisplay();
        Text authorComponent = Text.translatable("dispair.format.author", authorName).copy().setStyle(Style.EMPTY.withColor(color.getRGB())
                .withBold(true));
        Text bodyComponent = Text.of(bodyString).copy().setStyle(Style.EMPTY.withColor(Formatting.WHITE)
                .withBold(false));
        return authorComponent.copy().append(bodyComponent);
    }

    private Text formatAttachmentComponent(Message.Attachment attachment) {
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, attachment.getUrl());
        Text attachmentComponent = Text.translatable("dispair.format.attachment", attachment.getFileName());

        return attachmentComponent.copy().setStyle(
               Style.EMPTY.withClickEvent(clickEvent)
                       .withUnderline(true)
                       .withColor(Formatting.BLUE)
       );
    }

    private Text formatDiscordMessage(MessageReceivedEvent event) {
        Member author = event.getMember();
        if (author == null) {
            Dispair.LOGGER.error("Author of message was null, check your intents!");
            return null;
        }

        Message message = event.getMessage();
        Text body = formatBodyComponent(author, message);

        for (Message.Attachment attachment : event.getMessage().getAttachments()) {
            body = body.copy().append(formatAttachmentComponent(attachment));
        }

        return body;
    }
}
