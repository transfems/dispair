package lgbt.sylvia.dispair.screen;

import lgbt.sylvia.dispair.Configuration;
import lgbt.sylvia.dispair.Dispair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.awt.*;

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
            Configuration.save(Dispair.config);
            this.close();
        }).position((width/2)-75, height-28).build());

        this.addDrawableChild(tokenField);
        this.addDrawableChild(webhookField);
        this.addDrawableChild(channelField);
        this.addDrawableChild(activityField);
    }

    private void drawCentered(DrawContext context, String text, int y, int color) {
        MinecraftClient client = MinecraftClient.getInstance();
        context.drawCenteredTextWithShadow(client.textRenderer, text, this.width / 2, y, color);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), new Color(0x11111b).getRGB());

        super.render(context, mouseX, mouseY, delta);
        drawCentered(context, "Dispair", 5, Color.WHITE.getRGB());
        drawCentered(context, "Bot Token", 20, Color.WHITE.getRGB());
        drawCentered(context, "Webhook URI", 60, Color.WHITE.getRGB());
        drawCentered(context, "Channel", 100, Color.WHITE.getRGB());
        drawCentered(context, "Status", 140, Color.WHITE.getRGB());
        drawCentered(context, "Changes apply after restart!", context.getScaledWindowHeight() - 42, new Color(0xf38ba8).getRGB());
    }
}
