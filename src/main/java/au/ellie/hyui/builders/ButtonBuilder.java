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

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

public class ButtonBuilder extends UIElementBuilder<ButtonBuilder> {
    private String text;

    public ButtonBuilder() {
        super(UIElements.BUTTON);
        withWrappingGroup(true);
    }

    public ButtonBuilder(Theme theme) {
        super(theme, UIElements.BUTTON);
        withWrappingGroup(true);
        if (theme == Theme.GAME_THEME) {
            withUiFile("Pages/Elements/TextButton.ui");
        }
    }

    public ButtonBuilder(Theme theme, String elementPath) {
        super(theme, elementPath);
        withWrappingGroup(true);
        if (UIElements.TEXT_BUTTON.equals(elementPath)) {
            withUiFile("Pages/Elements/TextButton.ui");
        } else if (UIElements.CANCEL_TEXT_BUTTON.equals(elementPath)) {
            withUiFile("Pages/Elements/CancelTextButton.ui");
        } else if (UIElements.BACK_BUTTON.equals(elementPath)) {
            withUiFile("Pages/Elements/BackButton.ui");
        }
    }

    public static ButtonBuilder textButton() {
        return new ButtonBuilder(Theme.GAME_THEME, UIElements.TEXT_BUTTON);
    }

    public static ButtonBuilder cancelTextButton() {
        return new ButtonBuilder(Theme.GAME_THEME, UIElements.CANCEL_TEXT_BUTTON);
    }

    public ButtonBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public ButtonBuilder addEventListener(CustomUIEventBindingType type, Consumer<Void> callback) {
        return addEventListenerInternal(type, callback);
    }

    @Override
    protected boolean supportsStyling() {
        return true;
    }

    @Override
    protected Set<String> getUnsupportedStyleProperties() {
        if (this.theme == Theme.GAME_THEME) {
            return Set.of("FontSize", "TextColor");
        }
        return Collections.emptySet();
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
            HyUIPlugin.getInstance().logInfo("Setting Style: " + style + " for " + selector);
            commands.set(selector + ".Style", style);
        }

        listeners.forEach(listener -> {
            if (listener.type() == CustomUIEventBindingType.Activating) {
                String eventId = getEffectiveId();
                HyUIPlugin.getInstance().logInfo("Adding Activating event binding: " + eventId + " for " + selector);
                events.addEventBinding(CustomUIEventBindingType.Activating, selector, 
                        EventData.of("Action", UIEventActions.BUTTON_CLICKED)
                            .append("Target", eventId), 
                        false);
            }
        });
    }
}
