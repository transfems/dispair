package lgbt.sylvia.dispair.screen;

import lgbt.sylvia.dispair.util.TextureUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public class AttachmentScreen extends Screen {
    // WIP
    public AttachmentScreen(String uri) {
        super(Text.of("Attachment Viewer"));
        initWidgets(uri);
    }

    private Vector2i scaleToFit(float width, float height, float maxWidth, float maxHeight) {
        float widthRatio = maxWidth / width;
        float heightRatio = maxHeight / height;
        float smallestRatio = Math.min(widthRatio, heightRatio);
        return new Vector2i((int) (width * smallestRatio), (int) (height * heightRatio));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        Window window = minecraft.getWindow();
        TextureUtil.renderFilledQuad(matrices, 0, 0, window.getScaledWidth(), window.getScaledHeight(), 17, 17, 27, 255);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void initWidgets(String uri) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        try (NativeImage image = TextureUtil.downloadImage(uri)) {
            assert image != null;
            Window window = minecraft.getWindow();
            Vector2i bounds = new Vector2i(window.getScaledWidth() - 16, window.getScaledHeight() - 16);
            Vector2i size = scaleToFit(image.getWidth(), image.getHeight(), bounds.x(), bounds.y());
            Vector2i position = new Vector2i((window.getScaledWidth()/2) - (size.x()/2), (window.getScaledHeight()/2) - (size.y()/2));

            Identifier identifier = new Identifier("dispair", "image");
            TextureUtil.loadNativeImage(identifier, image);
            System.out.printf("scale / pos: %s %s / dim: %s %s%n%n", position.x(), position.y(), size.x(), size.y());
            //this.addDrawableChild(new ImageWidget(identifier, position.x(), position.y(), size.x(), size.y(), 0));
        }
    }
}
