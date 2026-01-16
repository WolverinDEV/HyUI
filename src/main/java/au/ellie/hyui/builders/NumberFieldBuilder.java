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

public class NumberFieldBuilder extends UIElementBuilder<NumberFieldBuilder> {
    private Double value;

    public NumberFieldBuilder() {
        super(UIElements.TEXT_FIELD);
        withWrappingGroup(true);
    }

    public NumberFieldBuilder(Theme theme) {
        super(theme, UIElements.MACRO_NUMBER_FIELD);
        withWrappingGroup(true);
        if (theme == Theme.GAME_THEME) {
            withUiFile("Pages/Elements/NumberInput.ui");
        }
    }

    public static NumberFieldBuilder numberInput() {
        return new NumberFieldBuilder(Theme.GAME_THEME);
    }

    public NumberFieldBuilder withValue(double value) {
        this.value = value;
        return this;
    }

    public NumberFieldBuilder addEventListener(CustomUIEventBindingType type, Consumer<Double> callback) {
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
                        EventData.of("@ValueDouble", selector + ".Value")
                            .append("Target", eventId)
                            .append("Action", UIEventActions.VALUE_CHANGED), 
                        false);
            }
        });
    }
}
