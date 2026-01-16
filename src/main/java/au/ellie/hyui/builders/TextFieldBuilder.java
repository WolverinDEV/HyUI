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

import java.util.Set;
import java.util.function.Consumer;

public class TextFieldBuilder extends UIElementBuilder<TextFieldBuilder> {
    private String value;

    public TextFieldBuilder() {
        super(UIElements.TEXT_FIELD);
        withWrappingGroup(true);
    }

    public TextFieldBuilder(Theme theme) {
        super(theme, UIElements.TEXT_FIELD);
        withWrappingGroup(true);
        if (theme == Theme.GAME_THEME) {
            withUiFile("Pages/Elements/TextInput.ui");
        }
    }

    public TextFieldBuilder(Theme theme, String elementPath) {
        super(theme, elementPath);
        withWrappingGroup(true);
        if (UIElements.MACRO_TEXT_FIELD.equals(elementPath)) {
            withUiFile("Pages/Elements/TextInput.ui");
        }
    }

    public static TextFieldBuilder textInput() {
        return new TextFieldBuilder(Theme.GAME_THEME, UIElements.MACRO_TEXT_FIELD);
    }

    public TextFieldBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public TextFieldBuilder addEventListener(CustomUIEventBindingType type, Consumer<String> callback) {
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
            HyUIPlugin.getInstance().logInfo("Setting Value: " + value + " for " + selector);
            commands.set(selector + ".Value", value);
        }

        if (hyUIStyle == null && style != null) {
            HyUIPlugin.getInstance().logInfo("Setting Style: " + style + " for " + selector);
            commands.set(selector + ".Style", style);
        }

        listeners.forEach(listener -> {
            if (listener.type() == CustomUIEventBindingType.ValueChanged) {
                String eventId = getEffectiveId();
                HyUIPlugin.getInstance().logInfo("Adding ValueChanged event binding for " + selector + " with eventId: " + eventId);
                events.addEventBinding(CustomUIEventBindingType.ValueChanged, selector, 
                        EventData.of("@Value", selector + ".Value")
                            .append("Target", eventId)
                            .append("Action", UIEventActions.VALUE_CHANGED), 
                        false);
            }
        });
    }
}
