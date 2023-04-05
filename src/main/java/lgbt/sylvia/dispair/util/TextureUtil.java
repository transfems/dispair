package lgbt.sylvia.dispair.util;

import com.mojang.blaze3d.systems.RenderSystem;
import lgbt.sylvia.dispair.Dispair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

    public static void renderTexture(Identifier texture, MatrixStack matrices, int x0, int y0, int x1, int y1, int z) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, (float) x0, (float) y1, (float) z).texture(0, 1).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z).texture(1, 1).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y0, (float) z).texture(1, 0).next();
        bufferBuilder.vertex(matrix, (float) x0, (float) y0, (float) z).texture(0, 0).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void renderFilledQuad(MatrixStack matrices, int x1, int y1, int x2, int y2, int r, int g, int b, int a) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        matrices.push();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        bufferBuilder
                .vertex(matrices.peek().getPositionMatrix(), x1, y2, (float) 0)
                .color(r, g, b, a)
                .next();
        bufferBuilder
                .vertex(matrices.peek().getPositionMatrix(), x2, y2, (float) 0)
                .color(r, g, b, a)
                .next();
        bufferBuilder
                .vertex(matrices.peek().getPositionMatrix(), x2, y1, (float) 0)
                .color(r, g, b, a)
                .next();
        bufferBuilder
                .vertex(matrices.peek().getPositionMatrix(), x1, y1, (float) 0)
                .color(r, g, b, a)
                .next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        matrices.pop();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
