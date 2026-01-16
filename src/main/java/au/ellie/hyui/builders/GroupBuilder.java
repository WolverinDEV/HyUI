package au.ellie.hyui.builders;

import au.ellie.hyui.HyUIPlugin;
import au.ellie.hyui.elements.UIElements;
import au.ellie.hyui.theme.Theme;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import java.util.Collections;
import java.util.Set;

public class GroupBuilder extends UIElementBuilder<GroupBuilder> {
    private String layoutMode;

    public GroupBuilder() {
        super(UIElements.GROUP);
    }

    public GroupBuilder(Theme theme) {
        super(theme, UIElements.GROUP);
    }

    public GroupBuilder withLayoutMode(String layoutMode) {
        this.layoutMode = layoutMode;
        return this;
    }

    @Override
    protected boolean supportsStyling() {
        return false;
    }

    @Override
    protected void onBuild(UICommandBuilder commands, UIEventBuilder events) {
        String selector = getSelector();
        if (selector == null) return;

        if (layoutMode != null) {
            HyUIPlugin.getInstance().logInfo("Setting LayoutMode: " + layoutMode + " for " + selector);
            commands.set(selector + ".LayoutMode", layoutMode);
        }

        if (hyUIStyle == null && style != null) {
            HyUIPlugin.getInstance().logInfo("Setting Style: " + style + " for " + selector);
            commands.set(selector + ".Style", style);
        }
    }
}
