/*
 (C)2024 sylvxa
 All Rights Reserved
*/
package lgbt.sylvia.dispair.mixin;

import lgbt.sylvia.dispair.Dispair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Text title) {
        super(title);
    }

    @Unique private Text getMessage() {
        return Text.of("Dispair is " + (Dispair.config.active ? "§aactive§r!" : "§cmuted§r!"));
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        CheckboxWidget widget =
                CheckboxWidget.builder(getMessage(), MinecraftClient.getInstance().textRenderer)
                        .pos(4, 4)
                        .checked(Dispair.config.active)
                        .callback(
                                (checkbox, checked) -> {
                                    Dispair.config.active = checked;
                                    checkbox.setMessage(getMessage());
                                })
                        .build();
        this.addDrawableChild(widget);
    }
}
