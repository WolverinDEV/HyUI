### UI Elements

This page provides examples of common UI element combinations and configurations in HyUI.

### Item Icon Button Example

It is often useful to combine a button with an item icon and labels to create interactive inventory-style elements. 
This example combines a button and item icon and labels within the button.

#### HYUIML Example

```html
<style>
    #IconButton {
        layout-mode: Left;
        padding: 6;
    }

    #IconButton:hover {
        background-color: #000000(0.2);
    }

    #Icon {
        anchor-width: 32;
        anchor-height: 32;
    }

    #ItemName {
        padding-left: 10;
        padding-right: 10;
        padding-top: 5;
        padding-bottom: 5;
        font-weight: bold;
        flex-weight: 1;
    }

    #ItemInfo {
        padding-left: 10;
        padding-right: 10;
        padding-top: 5;
        padding-bottom: 5;
        color: #ffffff;
    }
</style>

<button id="IconButton">
    <span id="Icon" class="item-icon" data-hyui-item-id="Tool_Pickaxe_Crude"></span>
    <p id="ItemName">Crude Pickaxe</p>
    <p id="ItemInfo">100/100</p>
</button>
```

#### Java Builder Example

```java
ButtonBuilder.textButton()
    .withId("IconButton")
    .withItemIcon(
        ItemIconBuilder.itemIcon()
            .withItemId("Tool_Pickaxe_Crude")
            .withAnchor(new HyUIAnchor().setWidth(32).setHeight(32))
    )
    .addChild(
        LabelBuilder.label()
            .withText("Crude Pickaxe")
            .withStyle(new HyUIStyle().setRenderBold(true))
    )
    .addChild(
        LabelBuilder.label()
            .withText("100/100")
    )
    .open(playerRef, store);
```
