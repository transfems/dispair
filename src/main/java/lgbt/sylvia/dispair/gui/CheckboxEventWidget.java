package lgbt.sylvia.dispair.gui;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

public class CheckboxEventWidget extends CheckboxWidget {
    private final CheckAction press;

    public CheckboxEventWidget(int x, int y, int width, int height, Text message, boolean checked, CheckAction press) {
        super(x, y, width, height, message, checked);
        this.press = press;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.press.run(this);
    }
}
