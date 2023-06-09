package lgbt.sylvia.dispair.mixin;

import lgbt.sylvia.dispair.screen.AttachmentScreen;
import lgbt.sylvia.dispair.screen.ConfigScreen;
import lgbt.sylvia.dispair.util.TextureUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    Identifier buttonTexture = TextureUtil.loadResource(Identifier.of("dispair", "icon_button"), "assets/dispair/textures/icon_button.png");
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.addDrawableChild(new TexturedButtonWidget(2, 2, 32, 32, 0, 0, 32, buttonTexture, 32, 64, (button) -> {
            assert this.client != null;
            //client.setScreen(new AttachmentScreen("https://cdn.discordapp.com/attachments/1019703878935134252/1092955047203196938/IMG_9656.jpg"));
            this.client.setScreen(new ConfigScreen());
        }));
    }
}
