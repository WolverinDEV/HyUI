package au.ellie.hyui.builders;

import au.ellie.hyui.HyUIPlugin;
import au.ellie.hyui.elements.UIElements;
import au.ellie.hyui.theme.Theme;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

public class LabelBuilder extends UIElementBuilder<LabelBuilder> {
    private String text;

    public LabelBuilder() {
        super(UIElements.LABEL);
    }

    public LabelBuilder(Theme theme) {
        super(theme, UIElements.LABEL);
    }

    public LabelBuilder withText(String text) {
        this.text = text;
        return this;
    }

    @Override
    protected boolean supportsStyling() {
        return true;
    }

    @Override
    protected void onBuild(UICommandBuilder commands, UIEventBuilder events) {
        String selector = getSelector();
        if (selector == null) return;

        if (text != null) {
            HyUIPlugin.getInstance().logInfo("Setting Text: " + text + " for " + selector);
            commands.set(selector + ".Text", text);
        }

        if (hyUIStyle == null && style != null) {
            HyUIPlugin.getInstance().logInfo("Setting Raw Style: " + style + " for " + selector);
            commands.set(selector + ".Style", style);
        }
    }
}
