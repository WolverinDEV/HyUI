package au.ellie.hyui.builders;

import au.ellie.hyui.HyUIPlugin;
import au.ellie.hyui.elements.UIElements;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

/**
 * Builder for the ItemIcon UI element.
 */
public class ItemIconBuilder extends UIElementBuilder<ItemIconBuilder> {
    private String itemId;

    public ItemIconBuilder() {
        super(UIElements.ITEM_ICON, "#HyUIItemIcon");
        withUiFile("Pages/Elements/ItemIcon.ui");
        withWrappingGroup(true);
    }

    /**
     * Factory method to create an ItemIconBuilder instance.
     *
     * @return an ItemIconBuilder configured for creating an ItemIcon element.
     */
    public static ItemIconBuilder itemIcon() {
        return new ItemIconBuilder();
    }

    /**
     * Sets the item ID for the item icon.
     *
     * @param itemId the item ID (e.g. Tool_Pickaxe_Crude).
     * @return this ItemIconBuilder instance.
     */
    public ItemIconBuilder withItemId(String itemId) {
        this.itemId = itemId;
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

        if (itemId != null) {
            HyUIPlugin.getLog().logFinest("Setting ItemId on " + selector + " to " + itemId);
            commands.set(selector + ".ItemId", itemId);
        }
    }
}
