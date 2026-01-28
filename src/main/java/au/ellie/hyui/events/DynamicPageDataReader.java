package au.ellie.hyui.events;

import au.ellie.hyui.utils.ParseUtils;

final class DynamicPageDataReader {
    private DynamicPageDataReader() {}

    static Integer getInt(DynamicPageData data, String key) {
        String value = data.getValue(key);
        if (value == null) {
            return null;
        }
        return ParseUtils.parseInt(value).orElse(null);
    }

    static String getString(DynamicPageData data, String key) {
        return data.getValue(key);
    }
}
