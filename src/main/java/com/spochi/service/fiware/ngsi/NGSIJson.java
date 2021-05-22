package com.spochi.service.fiware.ngsi;

import org.json.JSONObject;

public class NGSIJson {
    private final JSONObject json;

    public NGSIJson() {
        json = new JSONObject();
    }

    public NGSIJson id(String id) {
        json.put(NGSICommonFields.ID.getValue(), id);
        return this;
    }

    public NGSIJson type(String type) {
        json.put(NGSICommonFields.TYPE.getValue(), type);
        return this;
    }

    public NGSIJson attribute(NGSIField field, Object value) {
        final JSONObject attribute = new JSONObject();
        attribute.put(NGSICommonFields.TYPE.getValue(), field.getType());
        attribute.put(NGSICommonFields.VALUE.getValue(), value);

        json.put(field.getValue(), attribute);

        return this;
    }
}
