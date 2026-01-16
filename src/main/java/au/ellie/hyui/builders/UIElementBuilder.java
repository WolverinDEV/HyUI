package au.ellie.hyui.builders;

import au.ellie.hyui.HyUIPlugin;
import au.ellie.hyui.theme.Theme;
import au.ellie.hyui.events.UIEventListener;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class UIElementBuilder<T extends UIElementBuilder<T>> {
    protected final Theme theme;
    protected String elementPath;
    protected String uiFilePath;
    protected String id;
    protected String style;
    protected HyUIStyle hyUIStyle;
    protected final List<UIEventListener<?>> listeners = new ArrayList<>();
    protected final List<UIElementBuilder<?>> children = new ArrayList<>();
    protected String parentSelector = "#Content";
    protected int offset = -1;
    protected String typeSelector;
    protected boolean wrapInGroup = false;
    protected HyUIAnchor anchor;
    protected Boolean visible;
    protected Message tooltipTextSpan;
    protected final List<BiConsumer<UICommandBuilder, String>> editAfterCallbacks = new ArrayList<>();
    protected final List<BiConsumer<UICommandBuilder, String>> editBeforeCallbacks = new ArrayList<>();

    private static int idCounter = 0;

    public UIElementBuilder(String elementPath) {
        this(Theme.RAW, elementPath);
    }

    public UIElementBuilder(Theme theme, String elementPath) {
        this.theme = theme != null ? theme : Theme.RAW;
        this.elementPath = this.theme.format(elementPath);
        this.typeSelector = determineTypeSelector(elementPath);
        this.id = generateUniqueId();
    }

    private String generateUniqueId() {
        String base = elementPath;
        if (base != null) {
            // Get last part of the path
            int lastSlash = base.lastIndexOf('/');
            if (lastSlash != -1) {
                base = base.substring(lastSlash + 1);
            }
            // Replace .ui with blank
            base = base.replace(".ui", "")
                    .replace("$C.@", "");
        } else {
            base = "Element";
        }
        return base + (idCounter++);
    }

    protected String determineTypeSelector(String elementPath) {
        if (elementPath == null) return null;
        // Extract the base type from the element path
        // e.g., "$C.@TextButton" -> "#HyUITextButton"
        // e.g., "Group" -> "Group"
        if (elementPath.contains("TextButton")) return "#HyUITextButton";
        if (elementPath.contains("CancelTextButton")) return "#HyUICancelTextButton";
        if (elementPath.contains("TextField")) return "#HyUITextField";
        if (elementPath.contains("CheckBox")) return "#HyUICheckBox";
        if (elementPath.contains("ColorPicker")) return "#HyUIColorPicker";
        if (elementPath.contains("NumberField")) return "#HyUINumberField";
        if (elementPath.contains("Label")) return "Label";
        if (elementPath.contains("Group")) return "Group";
        return elementPath;
    }

    protected boolean isFilePath() {
        return elementPath != null && elementPath.endsWith(".ui");
    }

    public T withUiFile(String uiFilePath) {
        this.uiFilePath = uiFilePath;
        return (T) this;
    }

    public T addChild(UIElementBuilder<?> child) {
        this.children.add(child);
        return (T) this;
    }

    protected String getAppendPath() {
        if (uiFilePath != null) {
            return uiFilePath;
        }
        return elementPath;
    }

    protected String generateBasicInlineMarkup() {
        StringBuilder sb = new StringBuilder();
        sb.append(elementPath);
        if (id != null && !wrapInGroup) {
            sb.append(" #").append(id);
        }
        sb.append(" {}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public T withId(String id) {
        if (id != null) {
            this.id = id;
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withStyle(String style) {
        this.style = style;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withStyle(HyUIStyle style) {
        if (supportsStyling()) {
            this.hyUIStyle = style;
        }
        return (T) this;
    }

    protected boolean supportsStyling() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public T inside(String parentSelector) {
        this.parentSelector = parentSelector;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withWrappingGroup(boolean wrapInGroup) {
        this.wrapInGroup = wrapInGroup;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withAnchor(HyUIAnchor anchor) {
        this.anchor = anchor;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withVisible(boolean visible) {
        this.visible = visible;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T withTooltipTextSpan(Message message) {
        this.tooltipTextSpan = message;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T editElementAfter(BiConsumer<UICommandBuilder, String> callback) {
        this.editAfterCallbacks.add(callback);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T editElementBefore(BiConsumer<UICommandBuilder, String> callback) {
        this.editBeforeCallbacks.add(callback);
        return (T) this;
    }

    protected Set<String> getUnsupportedStyleProperties() {
        return Set.of();
    }

    @SuppressWarnings("unchecked")
    protected <V> T addEventListenerInternal(CustomUIEventBindingType type, Consumer<V> callback) {
        this.listeners.add(new UIEventListener<>(type, callback));
        return (T) this;
    }

    public void build(UICommandBuilder commands, UIEventBuilder events) {
        if (wrapInGroup && parentSelector != null) {
            String wrappingGroupId = getWrappingGroupId();
            HyUIPlugin.getInstance().logInfo("Creating wrapping group: #" + wrappingGroupId + " for element: " + (typeSelector != null ? typeSelector : elementPath));
            commands.appendInline(parentSelector, "Group #" + wrappingGroupId + " {}");
            
            // The inner element should be inside the wrapping group
            String originalParent = parentSelector;
            parentSelector = "#" + wrappingGroupId;
            executeBuild(commands, events);
            parentSelector = originalParent;
        } else {
            executeBuild(commands, events);
        }
    }

    private void executeBuild(UICommandBuilder commands, UIEventBuilder events) {
        buildBase(commands, events);

        String selector = getSelector();
        for (BiConsumer<UICommandBuilder, String> callback : editBeforeCallbacks) {
            callback.accept(commands, selector);
        }

        onBuild(commands, events);
        buildChildren(commands, events);

        for (BiConsumer<UICommandBuilder, String> callback : editAfterCallbacks) {
            callback.accept(commands, selector);
        }
    }

    protected void buildBase(UICommandBuilder commands, UIEventBuilder events) {
        String selector = getSelector();
        HyUIPlugin.getInstance().logInfo("Building element: " + (typeSelector != null ? typeSelector : elementPath) + " with ID: " + id + " at selector: " + selector);

        if (parentSelector != null) {
            String path = getAppendPath();
            if (path != null && path.endsWith(".ui")) {
                HyUIPlugin.getInstance().logInfo("Appending UI file: " + path + " to " + parentSelector);
                commands.append(parentSelector, path);
                
                // If it's a file but NOT wrapped, we need to set the ID of the root element in that file
                // if it's not already correct.
                if (!wrapInGroup && !id.equals(typeSelector != null ? typeSelector.replace("#", "") : "")) {
                     // We might need to rename the element we just appended.
                     // Let's assume for now that if it's not wrapped, it's a singleton or handled by user.
                }
            } else {
                String inline = generateBasicInlineMarkup();
                HyUIPlugin.getInstance().logInfo("Appending inline: " + inline + " to " + parentSelector);
                commands.appendInline(parentSelector, inline);
            }

            if (anchor != null) {
                HyUIPlugin.getInstance().logInfo("Setting Anchor for " + selector);
                commands.setObject(selector + ".Anchor", anchor.toHytaleAnchor());
            }

            if (visible != null) {
                HyUIPlugin.getInstance().logInfo("Setting Visible: " + visible + " for " + selector);
                commands.set(selector + ".Visible", visible);
            }

            if (tooltipTextSpan != null) {
                HyUIPlugin.getInstance().logInfo("Setting TooltipTextSpans for " + selector);
                commands.set(selector + ".TooltipTextSpans", tooltipTextSpan);
            }

            if (hyUIStyle != null) {
                applyStyle(commands, selector + ".Style", hyUIStyle);
            }
        }
    }

    protected void onBuild(UICommandBuilder commands, UIEventBuilder events) {
        // To be overridden by subclasses
    }

    protected void applyStyle(UICommandBuilder commands, String prefix, HyUIStyle style) {
        Set<String> unsupported = getUnsupportedStyleProperties();
        if (style.getFontSize() != null && !unsupported.contains("FontSize")) {
            HyUIPlugin.getInstance().logInfo("Setting Style FontSize: " + style.getFontSize() + " for " + prefix);
            commands.set(prefix + ".FontSize", style.getFontSize().doubleValue());
        }
        if (style.getRenderBold() != null && !unsupported.contains("RenderBold")) {
            HyUIPlugin.getInstance().logInfo("Setting Style RenderBold: " + style.getRenderBold() + " for " + prefix);
            commands.set(prefix + ".RenderBold", style.getRenderBold());
        }
        if (style.getRenderUppercase() != null && !unsupported.contains("RenderUppercase")) {
            HyUIPlugin.getInstance().logInfo("Setting Style RenderUppercase: " + style.getRenderUppercase() + " for " + prefix);
            commands.set(prefix + ".RenderUppercase", style.getRenderUppercase());
        }
        if (style.getTextColor() != null && !unsupported.contains("TextColor")) {
            HyUIPlugin.getInstance().logInfo("Setting Style TextColor: " + style.getTextColor() + " for " + prefix);
            commands.set(prefix + ".TextColor", style.getTextColor());
        }

        style.getRawProperties().forEach((key, value) -> {
            HyUIPlugin.getInstance().logInfo("Setting Style Raw Property: " + key + "=" + value + " for " + prefix);
            if (value instanceof String) {
                commands.set(prefix + "." + key, (String) value);
            } else if (value instanceof Boolean) {
                commands.set(prefix + "." + key, (Boolean) value);
            } else if (value instanceof Double) {
                commands.set(prefix + "." + key, (Double) value);
            } else if (value instanceof Integer) {
                commands.set(prefix + "." + key, (Integer) value);
            } else if (value instanceof Float) {
                commands.set(prefix + "." + key, ((Float) value).doubleValue());
            } else {
                commands.set(prefix + "." + key, String.valueOf(value));
            }
        });

        style.getStates().forEach((state, nestedStyle) -> {
            applyStyle(commands, prefix + "." + state, nestedStyle);
        });
    }

    protected String getWrappingGroupId() {
        return id;
    }

    protected void buildChildren(UICommandBuilder commands, UIEventBuilder events) {
        String selector = getSelector();
        if (selector != null) {
            for (UIElementBuilder<?> child : children) {
                child.inside(selector).build(commands, events);
            }
        }
    }

    public String getEffectiveId() {
        return id;
    }

    public String getElementPath() {
        return elementPath;
    }

    public String getId() {
        return id;
    }

    public List<UIEventListener<?>> getListeners() {
        return listeners;
    }

    protected String getSelector() {
        if (wrapInGroup) {
            return "#" + id + " " + typeSelector;
        }
        return "#" + id;
    }

    public static void resetIdCounter() {
        idCounter = 0;
    }
}
