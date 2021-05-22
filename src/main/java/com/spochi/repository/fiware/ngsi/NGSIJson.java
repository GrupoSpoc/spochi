package com.spochi.repository.fiware.ngsi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Function;

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

    public NGSIJson addAttribute(@NotNull NGSIField field, @NotNull Object value) {
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

    public String getAttributeValueString(NGSIField field) {
        return getAttributeValue(field, String::valueOf);
    }

    public Integer getAttributeValueInteger(NGSIField field) {
        return getAttributeValue(field, o -> Integer.parseInt(String.valueOf(o)));
    }

    public <T> T getAttributeValue(NGSIField field, Function<Object, T> mappingFunction) {
        try {
            final JSONObject attribute = this.getJSONObject(field.getName());
            return mappingFunction.apply(attribute.get(NGSICommonFields.VALUE.getName()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NGSIJsonException(e.getMessage());
        }
    }

    public static final class NGSIJsonException extends JSONException {
        public NGSIJsonException(String message) {
            super(message);
        }
    }
}


