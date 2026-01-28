package au.ellie.hyui.events;

/**
 * Payload passed to listeners registered for
 * {@link com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType#SlotMouseDragExited}.
 */
public final class SlotMouseDragExitedEventData {
    private final Integer mouseOverIndex;

    private final Integer slotIndex;

    public SlotMouseDragExitedEventData(Integer mouseOverIndex, Integer slotIndex) {
        this.mouseOverIndex = mouseOverIndex;
        this.slotIndex = slotIndex;
    }

    public Integer getMouseOverIndex() {
        return mouseOverIndex;
    }
    
    public Integer getSlotIndex() {
        return slotIndex;
    }

    public static SlotMouseDragExitedEventData from(DynamicPageData data) {
        return new SlotMouseDragExitedEventData(DynamicPageDataReader.getInt(data, "MouseOverIndex"),
                DynamicPageDataReader.getInt(data, "SlotIndex"));
    }
}
