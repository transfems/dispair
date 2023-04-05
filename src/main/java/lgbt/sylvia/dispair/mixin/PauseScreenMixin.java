package lgbt.sylvia.dispair.mixin;

import lgbt.sylvia.dispair.Dispair;
import lgbt.sylvia.dispair.gui.CheckAction;
import lgbt.sylvia.dispair.gui.CheckboxEventWidget;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Text title) {
        super(title);
    }

    private Text getMessage() {
        return Text.of("Dispair is " + (Dispair.config.muted ? "§cmuted§r!" : "§aactive§r!"));
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        CheckboxEventWidget widget = new CheckboxEventWidget(4, 4, 20, 20, getMessage(), Dispair.config.muted, new CheckAction() {
            @Override
            public void run(CheckboxWidget widget) {
                Dispair.config.muted = !widget.isChecked();
                widget.setMessage(getMessage());
            }
        });
        this.addDrawableChild(widget);
    }
}
