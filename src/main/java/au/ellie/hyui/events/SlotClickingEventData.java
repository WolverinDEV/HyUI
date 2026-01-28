package au.ellie.hyui.events;

/**
 * Payload passed to listeners registered for
 * {@link com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType#SlotClicking}.
 */
public final class SlotClickingEventData {
    private final Integer slotIndex;

    public SlotClickingEventData(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public static SlotClickingEventData from(DynamicPageData data) {
        return new SlotClickingEventData(DynamicPageDataReader.getInt(data, "SlotIndex"));
    }
}
