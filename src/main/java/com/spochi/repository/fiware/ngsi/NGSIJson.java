package com.spochi.repository.fiware.ngsi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class NGSIJson extends JSONObject {
    public NGSIJson(String source) {
        super(source);
    }

    public NGSIJson() {
        super();
    }

    public NGSIJson setId(@NotNull String id) {
        this.put(NGSICommonFields.ID.label(), id);
        return this;
    }

    public String getId() {
        return this.getString(NGSICommonFields.ID.label());
    }

    public NGSIJson setType(@NotNull NGSIEntityType type) {
        this.put(NGSICommonFields.TYPE.label(), type.label());
        return this;
    }

    public NGSIJson addAttribute(@NotNull NGSIField field, Object value) {
        if (value == null) return this;

        validateValue(field.type(), value);

        final JSONObject attribute = new JSONObject();

        attribute.put(NGSICommonFields.TYPE.label(), field.type().label());
        attribute.put(NGSICommonFields.VALUE.label(), value);

        this.put(field.label(), attribute);

        return this;
    }

    private void validateValue(NGSIFieldType type, Object value) {
        try {
            type.validateValue(value);
        } catch (NGSIFieldType.InvalidValueException e) {
            throw new NGSIJsonException(e.getMessage());
        }
    }

    public String getString(NGSIField field) {
        return getString(field.label());
    }

    public int getInt(NGSIField field) {
        return getInt(field.label());
    }

    public static final class NGSIJsonException extends JSONException {
        public NGSIJsonException(String message) {
            super(message);
        }
    }
}


