package com.spochi.service.fiware.ngsi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;

public class NGSIJson extends JSONObject {
    public NGSIJson(String source) throws JSONException {
        super(source);
    }

    public NGSIJson() {
        super();
    }

    public NGSIJson setId(String id) {
        this.put(NGSICommonFields.ID.getValue(), id);
        return this;
    }

    public String getId() {
        return this.getString(NGSICommonFields.ID.getValue());
    }

    public NGSIJson setType(String type) {
        this.put(NGSICommonFields.TYPE.getValue(), type);
        return this;
    }

    public NGSIJson addAttribute(NGSIField field, Object value) {
        final JSONObject attribute = new JSONObject();
        attribute.put(NGSICommonFields.TYPE.getValue(), field.getType());
        attribute.put(NGSICommonFields.VALUE.getValue(), value);

        this.put(field.getValue(), attribute);

        return this;
    }

    // todo null / mapping error
    public String getAttributeValueString(NGSIField field) {
        return getAttributeValue(field, String::valueOf);
    }

    public Integer getAttributeValueInteger(NGSIField field) {
        return getAttributeValue(field, o -> Integer.parseInt(String.valueOf(o)));
    }

    public <T> T getAttributeValue(NGSIField field, Function<Object, T> mappingFunction) {
        final JSONObject attribute = this.getJSONObject(field.getValue());

        return mappingFunction.apply(attribute.get(NGSICommonFields.VALUE.getValue()));
    }
}
