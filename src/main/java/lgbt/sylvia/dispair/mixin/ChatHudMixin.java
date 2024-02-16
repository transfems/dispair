/*
 (C)2024 sylvxa
 All Rights Reserved
*/
package lgbt.sylvia.dispair.mixin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lgbt.sylvia.dispair.Dispair;
import lgbt.sylvia.dispair.WebhookMessage;
import lgbt.sylvia.dispair.listener.MessageListener;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Unique private final Pattern pattern = Pattern.compile("<(.+)> (.+)");

    @Inject(
            method =
                    "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("TAIL"))
    public void onMessageAdd(
            Text message,
            MessageSignatureData signature,
            MessageIndicator indicator,
            CallbackInfo ci) {
        if (!Dispair.config.active) return;
        String rawContent = message.getString();
        System.out.println(rawContent);
        if (MessageListener.lastSentToPlayer.getString().equals(rawContent)) return;
        Matcher matcher = pattern.matcher(rawContent);
        if (matcher.find()) {
            String username = matcher.group(1);
            String content = matcher.group(2);

            MessageListener.lastSentToDiscord = content;
            WebhookMessage webhookMessage =
                    new WebhookMessage(username, "https://minotar.net/helm/" + username, content);
            webhookMessage.send(Dispair.config.webhook);
        } else {
            MessageListener.lastSentToDiscord = rawContent;
            WebhookMessage webhookMessage = new WebhookMessage(null, null, rawContent);
            webhookMessage.send(Dispair.config.webhook);
        }
    }
}
