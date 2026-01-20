### Page Building

Pages in HyUI are full-screen user interfaces that typically handle player interaction while the game is paused or the cursor is active. They are managed via the `PageBuilder`.

#### Content Sources

There are multiple ways to define the content of your page:

##### 1. Loading from UI Files
You can load an existing Hytale `.ui` file as the base for your page. HyUI often uses a "Placeholder" UI that provides a standard container and title.

```java
PageBuilder.pageForPlayer(playerRef)
    .fromFile("Pages/MyPage.ui")
    .open(store);
```

##### 2. Loading from HYUIML (HTML)
You can define your page using HTML-like syntax, which is often easier for complex layouts.

When using HYUIML for a page, it is recommended to use the standard structural classes:
- `.page-overlay`: Ensures the UI fills the screen and handles background dimming.
- `.container`: Provides the standard Hytale window frame.
- `.container-contents`: The main area for your content.

```java
String html = """
    <div class="page-overlay">
        <div class="container" data-hyui-title="My Custom Page">
            <div class="container-contents">
                <p>Welcome to my custom page built with HYUIML!</p>
                <button id="hi-btn">Say Hi</button>
            </div>
        </div>
    </div>
    """;

PageBuilder.pageForPlayer(playerRef)
    .fromHtml(html)
    .addEventListener("hi-btn", CustomUIEventBindingType.Activating, (ignored, ctx) -> {
        playerRef.sendMessage(Message.raw("Hello from the UI!"));
    })
    .open(store);
```

##### 3. Manual Building
You can manually add elements using builders for fine-grained control.

```java
PageBuilder.detachedPage()
    .withLifetime(CustomPageLifetime.CanDismiss)
    .addElement(PageOverlayBuilder.pageOverlay()
        .withId("MyOverlay")
        .addChild(ContainerBuilder.container()
            .withTitleText("Custom UI from scratch")
            .addContentChild(
                LabelBuilder.label()
                    .withText("Overlay Content")
            )
        )
    )
    .addElement(ButtonBuilder.backButton())
    .open(playerRef, store);
```

#### Detached Pages

Just like HUDs, you can prepare a page configuration before you have a player reference.

```java
PageBuilder builder = PageBuilder.detachedPage()
    .fromHtml("<p>Hello World</p>")
    .withLifetime(CustomPageLifetime.CanDismiss);

// Later, open it for a specific player
builder.open(playerRef, store);
```

#### Page Lifetime

You can control how the page behaves when the player tries to close it or when other UIs open.

```java
PageBuilder.pageForPlayer(playerRef)
    .withLifetime(CustomPageLifetime.CanDismiss) // Standard dismissable page
    .open(store);
```

#### Modifying Base Elements

If you load from a file, you can use `.editElement` to modify components defined in the `.ui` file before your dynamic elements are added.

```java
PageBuilder.pageForPlayer(playerRef)
    .fromFile("Pages/EllieAU_HyUI_Placeholder.ui")
    .editElement(commands -> {
        commands.set("#HyUITitle.Text", "Dynamic Title");
    })
    .open(store);
```

#### Complete Implementation Example

For a full example of a command that opens a variety of different page types, see the `HyUITestGuiCommand.java` file in the source code.
