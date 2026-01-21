package au.ellie.hyui.html.handlers;

import au.ellie.hyui.builders.ItemIconBuilder;
import au.ellie.hyui.builders.UIElementBuilder;
import au.ellie.hyui.html.HtmlParser;
import au.ellie.hyui.html.TagHandler;
import org.jsoup.nodes.Element;

public class ItemIconHandler implements TagHandler {
    @Override
    public boolean canHandle(Element element) {
        return element.tagName().equalsIgnoreCase("span") && element.hasClass("item-icon");
    }

    @Override
    public UIElementBuilder<?> handle(Element element, HtmlParser parser) {
        ItemIconBuilder builder = ItemIconBuilder.itemIcon();

        if (element.hasAttr("data-hyui-item-id")) {
            builder.withItemId(element.attr("data-hyui-item-id"));
        } else if (element.hasAttr("src")) {
            builder.withItemId(element.attr("src"));
        }
        
        applyCommonAttributes(builder, element);
        
        return builder;
    }
}
