package au.ellie.hyui.builders;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PageBuilder {
    private final PlayerRef playerRef;
    private CustomPageLifetime lifetime = CustomPageLifetime.CanDismiss;
    private final List<UIElementBuilder<?>> elements = new ArrayList<>();
    private final List<Consumer<UICommandBuilder>> editCallbacks = new ArrayList<>();
    private String uiFile;

    public PageBuilder(PlayerRef playerRef) {
        this.playerRef = playerRef;
    }

    public PageBuilder withLifetime(CustomPageLifetime lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public PageBuilder fromFile(String uiFile) {
        this.uiFile = uiFile;
        return this;
    }

    public PageBuilder addElement(UIElementBuilder<?> element) {
        this.elements.add(element);
        return this;
    }

    public PageBuilder editElement(Consumer<UICommandBuilder> callback) {
        this.editCallbacks.add(callback);
        return this;
    }

    public void open(Store<EntityStore> store) {
        Player playerComponent = store.getComponent(playerRef.getReference(), Player.getComponentType());
        PageManager pageManager = playerComponent.getPageManager();
        pageManager.openCustomPage(playerRef.getReference(), store, new HyUIPage(playerRef, lifetime, uiFile, elements, editCallbacks));
    }
}
