package lgbt.sylvia.dispair.util;

import com.mojang.blaze3d.systems.RenderSystem;
import lgbt.sylvia.dispair.Dispair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class TextureUtil {
    public static Identifier loadResource(Identifier identifier, String path) {
        InputStream resourceAsStream = Objects.requireNonNull(Dispair.class.getClassLoader().getResourceAsStream(path));
        try (NativeImage image = NativeImage.read(resourceAsStream)) {
            loadNativeImage(identifier, image);
        } catch (IOException ignored) {}
        return identifier;
    }

    public static NativeImage downloadImage(String uri) {
        try {
            return NativeImage.read(new URL(uri).openStream());
        } catch (IOException ignored) {}
        return null;
    }

    public static void loadNetworkResource(Identifier identifier, String uri) {
        NativeImage image = downloadImage(uri);
        loadNativeImage(identifier, image);
    }

    public static void loadNativeImage(Identifier identifier, NativeImage image) {
        MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, new NativeImageBackedTexture(image));
    }

    public static File saveScreenshot(File out) {
        Framebuffer framebuffer = MinecraftClient.getInstance().getFramebuffer();
        int width = framebuffer.textureWidth;
        int height = framebuffer.textureHeight;
        try (NativeImage nativeImage = new NativeImage(width, height, false)) {
            RenderSystem.bindTexture(framebuffer.getColorAttachment());
            nativeImage.loadFromTextureImage(0, true);
            nativeImage.mirrorVertically();
            nativeImage.writeTo(out);
        } catch (IOException ignored) {}
        return out;
    }
}
