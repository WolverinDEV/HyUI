package au.ellie.hyui.builders;

import au.ellie.hyui.elements.BackgroundSupported;
import au.ellie.hyui.elements.LayoutModeSupported;
import au.ellie.hyui.elements.UIElements;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;

/**
 * Builder for tab content sections linked to a tab ID.
 */
public class TabContentBuilder extends UIElementBuilder<TabContentBuilder>
        implements LayoutModeSupported<TabContentBuilder>, BackgroundSupported<TabContentBuilder> {
    private String tabId;
    private String tabNavigationId;
    private HyUIPatchStyle background;
    private String layoutMode;

    public TabContentBuilder() {
        super(UIElements.GROUP, "Group");
    }

    public static TabContentBuilder tabContent() {
        return new TabContentBuilder();
    }

    /**
     * Sets the tab ID that controls this content.
     */
    public TabContentBuilder withTabId(String tabId) {
        this.tabId = tabId;
        return this;
    }

    /**
     * Optionally restricts this content to a specific tab navigation builder ID.
     */
    public TabContentBuilder withTabNavigationId(String tabNavigationId) {
        this.tabNavigationId = tabNavigationId;
        return this;
    }

    public String getTabId() {
        return tabId;
    }

    public String getTabNavigationId() {
        return tabNavigationId;
    }

    @Override
    public TabContentBuilder withLayoutMode(String layoutMode) {
        this.layoutMode = layoutMode;
        return this;
    }

    @Override
    public String getLayoutMode() {
        return this.layoutMode;
    }

    @Override
    public TabContentBuilder withBackground(HyUIPatchStyle background) {
        this.background = background;
        return this;
    }

    @Override
    public HyUIPatchStyle getBackground() {
        return this.background;
    }

    @Override
    protected void onBuild(UICommandBuilder commands, UIEventBuilder events) {
        String selector = getSelector();
        if (selector == null) return;

        applyLayoutMode(commands, selector);
        applyBackground(commands, selector);
    }

    @Override
    protected boolean supportsStyling() {
        return false;
    }
}
