package au.ellie.hyui.theme;

public enum Theme {
    RAW(""),
    GAME_THEME("$C.@");

    private final String prefix;

    Theme(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String format(String element) {
        if (this == RAW) {
            return element;
        }
        return prefix + element;
    }
}
