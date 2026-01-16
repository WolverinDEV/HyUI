package au.ellie.hyui.events;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import java.util.HashMap;
import java.util.Map;

public class DynamicPageData {
    public static final BuilderCodec<DynamicPageData> CODEC = BuilderCodec.builder(DynamicPageData.class, DynamicPageData::new)
            .addField(new KeyedCodec<>("Action", Codec.STRING), (data, s) -> data.action = s, data -> data.action)
            .addField(new KeyedCodec<>("@Value", Codec.STRING), (data, s) -> data.values.put("RefValue", s), data -> data.values.get("RefValue"))
            .addField(new KeyedCodec<>("@ValueBool", Codec.BOOLEAN), (data, s) -> data.values.put("RefValue", String.valueOf(s)), data -> Boolean.parseBoolean(data.values.get("RefValue")))
            .addField(new KeyedCodec<>("@ValueInt", Codec.INTEGER), (data, s) -> data.values.put("RefValue", String.valueOf(s)), data -> Integer.parseInt(data.values.get("RefValue")))
            .addField(new KeyedCodec<>("@ValueFloat", Codec.FLOAT), (data, s) -> data.values.put("RefValue", String.valueOf(s)), data -> Float.parseFloat(data.values.get("RefValue")))
            .addField(new KeyedCodec<>("@ValueDouble", Codec.DOUBLE), (data, s) -> data.values.put("RefValue", String.valueOf(s)), data -> Double.parseDouble(data.values.get("RefValue")))
            .addField(new KeyedCodec<>("Value", Codec.STRING), (data, s) -> data.values.put("Value", s), data -> data.values.get("Value"))
            .addField(new KeyedCodec<>("Target", Codec.STRING), (data, s) -> data.values.put("Target", s), data -> data.values.get("Target"))
            .build();

    public String action;
    public final Map<String, String> values = new HashMap<>();

    public String getValue(String key) {
        return values.get(key);
    }
}
