package lgbt.sylvia.dispair.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import lgbt.sylvia.dispair.Dispair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

import lgbt.sylvia.dispair.util.TextureUtil;

public class ConfigScreen extends Screen {
    public ConfigScreen() {
        super(Text.of("dispair config"));

        initWidgets();
    }

    private void initWidgets() {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        TextFieldWidget tokenField = new TextFieldWidget(client.textRenderer, 8, 30, (width)-16, 20, Text.of(Dispair.config.token));
        tokenField.setMaxLength(128);
        tokenField.setText(Dispair.config.token);
        TextFieldWidget webhookField = new TextFieldWidget(client.textRenderer, 8, 70, (width)-16, 20, Text.of(Dispair.config.webhook));
        webhookField.setMaxLength(128);
        webhookField.setText(Dispair.config.webhook);
        TextFieldWidget channelField = new TextFieldWidget(client.textRenderer, 8, 110, (width)-16, 20, Text.of(Dispair.config.channel));
        channelField.setMaxLength(32);
        channelField.setText(Dispair.config.channel);
        TextFieldWidget activityField = new TextFieldWidget(client.textRenderer, 8, 150, (width)-16, 20, Text.of(Dispair.config.activity));
        activityField.setMaxLength(128);
        activityField.setText(Dispair.config.activity);

        this.addDrawableChild(new ButtonWidget.Builder(Text.of("Save"), (button) -> {
            Dispair.config.token = tokenField.getText();
            Dispair.config.webhook = webhookField.getText();
            Dispair.config.channel = channelField.getText();
            Dispair.config.activity = activityField.getText();
            this.close();
        }).position((width/2)-75, height-28).build());

        this.addDrawableChild(tokenField);
        this.addDrawableChild(webhookField);
        this.addDrawableChild(channelField);
        this.addDrawableChild(activityField);
    }

    private void drawCentered(MatrixStack matrices, String text, int y, int color) {
        int x = (this.width/2) - (this.textRenderer.getWidth(text)/2);
        this.textRenderer.drawWithShadow(matrices, text, x, y, color);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        TextureUtil.renderFilledQuad(matrices, 0, 0, width, height, 17, 17, 27, 255);

        super.render(matrices, mouseX, mouseY, delta);
        drawCentered(matrices, "Dispair", 5, Color.WHITE.getRGB());
        drawCentered(matrices, "Bot Token", 20, Color.WHITE.getRGB());
        drawCentered(matrices, "Webhook URI", 60, Color.WHITE.getRGB());
        drawCentered(matrices, "Channel", 100, Color.WHITE.getRGB());
        drawCentered(matrices, "Status", 140, Color.WHITE.getRGB());
    }
}
