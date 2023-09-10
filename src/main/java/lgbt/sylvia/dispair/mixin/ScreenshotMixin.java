package lgbt.sylvia.dispair.mixin;

import lgbt.sylvia.dispair.Dispair;
import lgbt.sylvia.dispair.util.TextureUtil;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotMixin {
    @Unique
    private static final File screenshotDirectory = new File(MinecraftClient.getInstance().runDirectory, "screenshots");

    @Inject(at = @At(value = "HEAD"), method = "takeScreenshot")
    private static void takeScreenshot(Framebuffer framebuffer, CallbackInfoReturnable<NativeImage> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        File file = new File(screenshotDirectory, Util.getFormattedCurrentTime() + ".png");
        if (!file.exists()) return;
        FileUpload fileUpload = FileUpload.fromData(TextureUtil.saveScreenshot(file));
        TextChannel channel = Dispair.jda.getChannelById(TextChannel.class, Dispair.config.channel);
        if (client.player == null) return;
        if (channel != null) {
            channel.sendFiles(fileUpload).queue();
            client.player.sendMessage(Text.of("§aSent screenshot to Discord channel!"));
        } else {
            client.player.sendMessage(Text.of("§cUnable to find channel, upload failed!"));
        }
    }

    @ModifyVariable(at = @At(value = "HEAD"), method = "saveScreenshot(Ljava/io/File;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V", ordinal = 0, argsOnly = true)
    private static Consumer<Text> cancelConsumer(Consumer<Text> consumer) {
        return text -> {};
    }
}
