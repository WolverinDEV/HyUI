package au.ellie.hyui.events;

/**
 * Payload passed to listeners registered for
 * {@link com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType#SlotMouseDragCompleted}.
 */
public final class SlotMouseDragCompletedEventData {
    private final Integer sourceItemGridIndex;
    private final Integer sourceSlotId;
    
    private final Integer slotIndex;
    private final Integer itemStackQuantity;
    private final Integer pressedMouseButton;
    private final String itemStackId;
    private final String sourceInventorySectionId;

    public SlotMouseDragCompletedEventData(Integer sourceItemGridIndex,
                                           Integer sourceSlotId,
                                           Integer itemStackQuantity,
                                           Integer pressedMouseButton,
                                           String itemStackId,
                                           String sourceInventorySectionId,
                                           Integer slotIndex) {
        this.sourceItemGridIndex = sourceItemGridIndex;
        this.sourceSlotId = sourceSlotId;
        this.itemStackQuantity = itemStackQuantity;
        this.pressedMouseButton = pressedMouseButton;
        this.itemStackId = itemStackId;
        this.sourceInventorySectionId = sourceInventorySectionId;
        this.slotIndex = slotIndex;
    }

    public Integer getSourceItemGridIndex() {
        return sourceItemGridIndex;
    }

    public Integer getSourceSlotId() {
        return sourceSlotId;
    }

    public Integer getItemStackQuantity() {
        return itemStackQuantity;
    }

    public Integer getPressedMouseButton() {
        return pressedMouseButton;
    }

    public String getItemStackId() {
        return itemStackId;
    }
    
    public Integer getSlotIndex() {
        return slotIndex;
    }

    public String getSourceInventorySectionId() {
        return sourceInventorySectionId;
    }

    public static SlotMouseDragCompletedEventData from(DynamicPageData data) {
        return new SlotMouseDragCompletedEventData(
                DynamicPageDataReader.getInt(data, "SourceItemGridIndex"),
                DynamicPageDataReader.getInt(data, "SourceSlotId"),
                DynamicPageDataReader.getInt(data, "ItemStackQuantity"),
                DynamicPageDataReader.getInt(data, "PressedMouseButton"),
                DynamicPageDataReader.getString(data, "ItemStackId"),
                DynamicPageDataReader.getString(data, "SourceInventorySectionId"),
                DynamicPageDataReader.getInt(data, "SlotIndex")
        );
    }
}
