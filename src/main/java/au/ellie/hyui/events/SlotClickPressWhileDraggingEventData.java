package au.ellie.hyui.events;

/**
 * Payload passed to listeners registered for
 * {@link com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType#SlotClickPressWhileDragging}.
 */
public final class SlotClickPressWhileDraggingEventData {
    private final Integer slotIndex;
    private final String dragItemStackId;
    private final Integer dragItemStackQuantity;
    private final String dragSourceInventorySectionId;
    private final Integer dragSourceItemGridIndex;
    private final Integer dragSourceSlotId;
    private final Integer dragPressedMouseButton;
    private final Integer clickMouseButton;
    private final Integer clickCount;

    public SlotClickPressWhileDraggingEventData(Integer slotIndex,
                                                String dragItemStackId,
                                                Integer dragItemStackQuantity,
                                                String dragSourceInventorySectionId,
                                                Integer dragSourceItemGridIndex,
                                                Integer dragSourceSlotId,
                                                Integer dragPressedMouseButton,
                                                Integer clickMouseButton,
                                                Integer clickCount) {
        this.slotIndex = slotIndex;
        this.dragItemStackId = dragItemStackId;
        this.dragItemStackQuantity = dragItemStackQuantity;
        this.dragSourceInventorySectionId = dragSourceInventorySectionId;
        this.dragSourceItemGridIndex = dragSourceItemGridIndex;
        this.dragSourceSlotId = dragSourceSlotId;
        this.dragPressedMouseButton = dragPressedMouseButton;
        this.clickMouseButton = clickMouseButton;
        this.clickCount = clickCount;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public String getDragItemStackId() {
        return dragItemStackId;
    }

    public Integer getDragItemStackQuantity() {
        return dragItemStackQuantity;
    }

    public String getDragSourceInventorySectionId() {
        return dragSourceInventorySectionId;
    }

    public Integer getDragSourceItemGridIndex() {
        return dragSourceItemGridIndex;
    }

    public Integer getDragSourceSlotId() {
        return dragSourceSlotId;
    }

    public Integer getDragPressedMouseButton() {
        return dragPressedMouseButton;
    }

    public Integer getClickMouseButton() {
        return clickMouseButton;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public static SlotClickPressWhileDraggingEventData from(DynamicPageData data) {
        return new SlotClickPressWhileDraggingEventData(
                DynamicPageDataReader.getInt(data, "SlotIndex"),
                DynamicPageDataReader.getString(data, "DragItemStackId"),
                DynamicPageDataReader.getInt(data, "DragItemStackQuantity"),
                DynamicPageDataReader.getString(data, "DragSourceInventorySectionId"),
                DynamicPageDataReader.getInt(data, "DragSourceItemGridIndex"),
                DynamicPageDataReader.getInt(data, "DragSourceSlotId"),
                DynamicPageDataReader.getInt(data, "DragPressedMouseButton"),
                DynamicPageDataReader.getInt(data, "ClickMouseButton"),
                DynamicPageDataReader.getInt(data, "ClickCount")
        );
    }
}
