package au.ellie.hyui.events;

/**
 * Payload passed to listeners registered for
 * {@link com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType#SlotMouseEntered}.
 */
public final class SlotMouseEnteredEventData {
    private final Integer slotIndex;

    public SlotMouseEnteredEventData(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public static SlotMouseEnteredEventData from(DynamicPageData data) {
        return new SlotMouseEnteredEventData(DynamicPageDataReader.getInt(data, "SlotIndex"));
    }
}
