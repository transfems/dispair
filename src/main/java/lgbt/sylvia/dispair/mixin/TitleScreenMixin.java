/*
 (C)2024 sylvxa
 All Rights Reserved
*/
package lgbt.sylvia.dispair.mixin;

import lgbt.sylvia.dispair.screen.ConfigScreen;
import net.minecraft.client.gui.screen.ButtonTextures;
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
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        ButtonTextures buttonTextures =
                new ButtonTextures(
                        Identifier.of("dispair", "icon"), Identifier.of("dispair", "icon_shiny"));
        this.addDrawableChild(
                new TexturedButtonWidget(
                        2,
                        2,
                        32,
                        32,
                        buttonTextures,
                        (button) -> {
                            assert this.client != null;
                            this.client.setScreen(new ConfigScreen());
                        }));
    }
}
