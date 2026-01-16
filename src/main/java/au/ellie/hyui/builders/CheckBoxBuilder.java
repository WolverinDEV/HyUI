package au.ellie.hyui.builders;

import au.ellie.hyui.HyUIPlugin;
import au.ellie.hyui.events.UIEventActions;
import au.ellie.hyui.elements.UIElements;
import au.ellie.hyui.theme.Theme;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

import java.util.function.Consumer;

public class CheckBoxBuilder extends UIElementBuilder<CheckBoxBuilder> {
    private Boolean value;
    private String text;

    public CheckBoxBuilder() {
        super(UIElements.CHECK_BOX_WITH_LABEL);
        withWrappingGroup(true);
        withUiFile("Pages/Elements/CheckBox.ui");
    }

    public CheckBoxBuilder(Theme theme) {
        super(theme, UIElements.CHECK_BOX_WITH_LABEL);
        withWrappingGroup(true);
        withUiFile("Pages/Elements/CheckBox.ui");
    }

    public CheckBoxBuilder withValue(boolean value) {
        this.value = value;
        return this;
    }

    public CheckBoxBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public CheckBoxBuilder addEventListener(CustomUIEventBindingType type, Consumer<Boolean> callback) {
        return addEventListenerInternal(type, callback);
    }

    @Override
    protected boolean supportsStyling() {
        return true;
    }

    @Override
    protected void onBuild(UICommandBuilder commands, UIEventBuilder events) {
        String selector = getSelector();
        if (selector == null) return;

        if (value != null) {
            // For CheckBoxWithLabel, the actual CheckBox is a child
            HyUIPlugin.getInstance().logInfo("Setting Value: " + value + " for " + selector + " #CheckBox");
            commands.set(selector + " #CheckBox.Value", value);
        }

        if (text != null) {
            HyUIPlugin.getInstance().logInfo("Setting Text: " + text + " for " + selector);
            commands.set(selector + ".@Text", text);
        }

        if (hyUIStyle == null && style != null) {
            HyUIPlugin.getInstance().logInfo("Setting Style: " + style + " for " + selector);
            commands.set(selector + ".Style", style);
        }

        listeners.forEach(listener -> {
            if (listener.type() == CustomUIEventBindingType.ValueChanged) {
                String eventId = getEffectiveId();
                HyUIPlugin.getInstance().logInfo("Adding ValueChanged event binding for " + selector + " #CheckBox with eventId: " + eventId);
                events.addEventBinding(CustomUIEventBindingType.ValueChanged, selector + " #CheckBox", 
                        EventData.of("@ValueBool", selector + " #CheckBox.Value")
                            .append("Target", eventId)
                            .append("Action", UIEventActions.VALUE_CHANGED),
                        false);
            }
        });
    }
}
