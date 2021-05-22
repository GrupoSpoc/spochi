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
        this.put(NGSICommonFields.ID.getName(), id);
        return this;
    }

    public String getId() {
        return this.getString(NGSICommonFields.ID.getName());
    }

    public NGSIJson setType(@NotNull String type) {
        this.put(NGSICommonFields.TYPE.getName(), type);
        return this;
    }

    public NGSIJson addAttribute(@NotNull NGSIField field, Object value) {
        if (value == null) return this;

        validateValue(field.getType(), value);

        final JSONObject attribute = new JSONObject();

        attribute.put(NGSICommonFields.TYPE.getName(), field.getType().getName());
        attribute.put(NGSICommonFields.VALUE.getName(), value);

        this.put(field.getName(), attribute);

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
        return getString(field.getName());
    }

    public int getInt(NGSIField field) {
        return getInt(field.getName());
    }

    public static final class NGSIJsonException extends JSONException {
        public NGSIJsonException(String message) {
            super(message);
        }
    }
}


