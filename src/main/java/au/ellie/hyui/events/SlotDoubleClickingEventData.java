package au.ellie.hyui.events;

/**
 * Payload passed to listeners registered for
 * {@link com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType#SlotDoubleClicking}.
 */
public final class SlotDoubleClickingEventData {
    private final Integer slotIndex;

    public SlotDoubleClickingEventData(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public static SlotDoubleClickingEventData from(DynamicPageData data) {
        return new SlotDoubleClickingEventData(DynamicPageDataReader.getInt(data, "SlotIndex"));
    }
}
