package au.ellie.hyui.events;

/**
 * Payload passed to listeners registered for
 * {@link com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType#SlotClickReleaseWhileDragging}.
 */
public final class SlotClickReleaseWhileDraggingEventData {
    private final Integer slotIndex;
    private final Integer clickMouseButton;
    private final Integer clickCount;

    public SlotClickReleaseWhileDraggingEventData(Integer slotIndex, Integer clickMouseButton, Integer clickCount) {
        this.slotIndex = slotIndex;
        this.clickMouseButton = clickMouseButton;
        this.clickCount = clickCount;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public Integer getClickMouseButton() {
        return clickMouseButton;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public static SlotClickReleaseWhileDraggingEventData from(DynamicPageData data) {
        return new SlotClickReleaseWhileDraggingEventData(
                DynamicPageDataReader.getInt(data, "SlotIndex"),
                DynamicPageDataReader.getInt(data, "ClickMouseButton"),
                DynamicPageDataReader.getInt(data, "ClickCount")
        );
    }
}
