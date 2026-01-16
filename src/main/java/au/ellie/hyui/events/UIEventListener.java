package au.ellie.hyui.events;

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import java.util.function.Consumer;

public record UIEventListener<V>(CustomUIEventBindingType type, Consumer<V> callback) {
}
