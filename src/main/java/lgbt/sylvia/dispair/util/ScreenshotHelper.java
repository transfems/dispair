package lgbt.sylvia.dispair.util;

import com.mojang.blaze3d.systems.RenderSystem;
import lgbt.sylvia.dispair.Dispair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;

import java.io.File;
import java.io.IOException;

public class ScreenshotHelper {
    public static File write(File out) {
        Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
        int width = framebuffer.textureWidth;
        int height = framebuffer.textureHeight;
        try (NativeImage nativeImage = new NativeImage(width, height, false)) {
            RenderSystem.bindTexture(framebuffer.getColorAttachment());
            nativeImage.loadFromTextureImage(0, true);
            nativeImage.mirrorVertically();
            nativeImage.writeTo(out);
        } catch (IOException exception) {
            Dispair.LOGGER.error("Couldn't write screenshot!");
            exception.printStackTrace();
        }
        return out;
    }
}
