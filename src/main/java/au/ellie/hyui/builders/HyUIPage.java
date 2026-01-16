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

    /**
     * Creates a new HyUIPage.
     *
     * @param playerRef     The player this page is for.
     * @param lifetime      The lifetime of the page.
     * @param baseUiFile    The base UI file to use (e.g. "Pages/Placeholder.ui").
     * @param elements      The elements to add to the page.
     */
    public HyUIPage(PlayerRef playerRef, CustomPageLifetime lifetime, String baseUiFile, List<UIElementBuilder<?>> elements) {
        this(playerRef, lifetime, baseUiFile, elements, null);
    }

    public HyUIPage(PlayerRef playerRef, CustomPageLifetime lifetime, String uiFile, List<UIElementBuilder<?>> elements, List<Consumer<UICommandBuilder>> editCallbacks) {
        super(playerRef, lifetime, DynamicPageData.CODEC);
        this.uiFile = uiFile;
        this.elements = elements;
        this.editCallbacks = editCallbacks;
    }

    /**
     * NOTE: Do not call this method, Hytale will call this method for you.
     * To open a UI see documentation.
     * 
     * Builds the HyUI page by appending the designated UI file and processing the 
     * elements and callbacks associated with the page.
     *
     * @param ref             The reference to the entity store associated with the page.
     * @param uiCommandBuilder The builder used to construct the UI commands for the page.
     * @param uiEventBuilder   The builder used to construct the UI events for the page.
     * @param store            The store containing the entity data required for the page.
     */
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
                        if (element.usesRefValue()) {
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
