package au.ellie.hyui.html.handlers;

import au.ellie.hyui.builders.ImageBuilder;
import au.ellie.hyui.builders.UIElementBuilder;
import au.ellie.hyui.html.HtmlParser;
import au.ellie.hyui.html.TagHandler;
import org.jsoup.nodes.Element;

public class ImgHandler implements TagHandler {
    @Override
    public boolean canHandle(Element element) {
        return element.tagName().equalsIgnoreCase("img");
    }

    @Override
    public UIElementBuilder<?> handle(Element element, HtmlParser parser) {
        ImageBuilder builder = ImageBuilder.image();
        
        if (element.hasAttr("src")) {
            builder.withImage(element.attr("src"));
        }
        
        applyCommonAttributes(builder, element);
        
        return builder;
    }
}
