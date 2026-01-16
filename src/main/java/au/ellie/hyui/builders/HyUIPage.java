package au.ellie.hyui.builders;

import au.ellie.hyui.HyUIPlugin;
import au.ellie.hyui.events.DynamicPageData;
import au.ellie.hyui.events.UIEventActions;
import au.ellie.hyui.events.UIEventListener;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class HyUIPage extends InteractiveCustomUIPage<DynamicPageData> {
    private final String uiFile;
    private final List<UIElementBuilder<?>> elements;
    private final List<Consumer<UICommandBuilder>> editCallbacks;

    public HyUIPage(PlayerRef playerRef, CustomPageLifetime lifetime, String uiFile, List<UIElementBuilder<?>> elements, List<Consumer<UICommandBuilder>> editCallbacks) {
        super(playerRef, lifetime, DynamicPageData.CODEC);
        this.uiFile = uiFile;
        this.elements = elements;
        this.editCallbacks = editCallbacks;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        HyUIPlugin.getInstance().logInfo("Building HyUIPage" + (uiFile != null ? " from file: " + uiFile : ""));
        if (uiFile != null) {
            uiCommandBuilder.append(uiFile);
        }

        if (editCallbacks != null) {
            for (Consumer<UICommandBuilder> callback : editCallbacks) {
                callback.accept(uiCommandBuilder);
            }
        }

        for (UIElementBuilder<?> element : elements) {
            element.build(uiCommandBuilder, uiEventBuilder);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull DynamicPageData data) {
        super.handleDataEvent(ref, store, data);

        HyUIPlugin.getInstance().logInfo("Received DataEvent: Action=" + data.action);
        data.values.forEach((key, value) -> {
            HyUIPlugin.getInstance().logInfo("  Property: " + key + " = " + value);
        });

        for (UIElementBuilder<?> element : elements) {
            handleElementEvents(element, data);
        }
    }

    private void handleElementEvents(UIElementBuilder<?> element, DynamicPageData data) {
        String effectiveId = element.getEffectiveId();

        if (effectiveId != null) {
            String target = data.getValue("Target");

            for (UIEventListener<?> listener : element.getListeners()) {
                if (listener.type() == CustomUIEventBindingType.Activating && UIEventActions.BUTTON_CLICKED.equals(data.action)) {
                    if (effectiveId.equals(target)) {
                        ((UIEventListener<Void>) listener).callback().accept(null);
                    }
                } else if (listener.type() == CustomUIEventBindingType.ValueChanged) {
                    String finalValue = null;

                    if (UIEventActions.VALUE_CHANGED.equals(data.action) && effectiveId.equals(target)) {
                        // If it's a value-changed action, use @Value (RefValue) for specific elements
                        if (element instanceof TextFieldBuilder || element instanceof NumberFieldBuilder || 
                            element instanceof CheckBoxBuilder || element instanceof ColorPickerBuilder) {
                            finalValue = data.getValue("RefValue");
                        } else {
                            finalValue = data.getValue("Value");
                        }
                    }

                    if (finalValue != null) {
                        if (element instanceof NumberFieldBuilder) {
                            try {
                                Double dValue = Double.parseDouble(finalValue);
                                ((UIEventListener<Double>) listener).callback().accept(dValue);
                            } catch (NumberFormatException ignored) {
                            }
                        } else if (element instanceof CheckBoxBuilder) {
                            Boolean bValue = Boolean.parseBoolean(finalValue);
                            ((UIEventListener<Boolean>) listener).callback().accept(bValue);
                        } else {
                            ((UIEventListener<String>) listener).callback().accept(finalValue);
                        }
                    }
                }
            }
        }

        for (UIElementBuilder<?> child : element.children) {
            handleElementEvents(child, data);
        }
    }
}
