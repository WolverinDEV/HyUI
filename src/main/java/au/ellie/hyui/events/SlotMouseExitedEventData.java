package au.ellie.hyui.events;

/**
 * Payload passed to listeners registered for
 * {@link com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType#SlotMouseExited}.
 */
public final class SlotMouseExitedEventData {
    private final Integer slotIndex;

    public SlotMouseExitedEventData(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public static SlotMouseExitedEventData from(DynamicPageData data) {
        return new SlotMouseExitedEventData(DynamicPageDataReader.getInt(data, "SlotIndex"));
    }
}
