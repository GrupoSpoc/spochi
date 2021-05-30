package com.spochi.repository.fiware.ngsi;

import com.spochi.util.AssertUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("NGSIJsonTest | Unit")
class NGSIJsonTest {

    @Test
    @DisplayName("instantiate | invalid source | JsonException")
    void instantiateInvalidSourceException() {
        assertThrows(JSONException.class, () -> new NGSIJson("not-a-json"));
    }

    @Test
    @DisplayName("set id | ok")
    void setId() {
        final String id = "test-id";

        final NGSIJson json = new NGSIJson();
        json.setId(id);

        assertEquals(id, json.getString(NGSICommonFields.ID.label()));
    }

    @Test
    @DisplayName("get id | when no id present | JsonException")
    void getId() {
        final NGSIJson json = new NGSIJson();

        assertThrows(JSONException.class, json::getId);
    }

    @Test
    @DisplayName("set type | ok")
    void setType() {
        final NGSIJson json = new NGSIJson();
        json.setType(NGSISerializableEntityForTest.NGSIType);

        assertEquals(NGSISerializableEntityForTest.NGSIType.label(), json.getString(NGSICommonFields.TYPE.label()));
    }

    @Test
    @DisplayName("add attribute | when value is invalid | NGSIJsonException")
    void addAttribute() {
        final String value = "a-value";
        final NGSIFieldType type = mock(NGSIFieldType.class);
        final NGSIField field = mock(NGSIField.class);

        when(field.type()).thenReturn(type);
        doThrow(new NGSIFieldType.InvalidValueException("error-message")).when(type).validateValue(value);

        final NGSIJson json = new NGSIJson();

        AssertUtils.assertException(NGSIJson.NGSIJsonException.class, () -> json.addAttribute(field, value), "error-message");
    }

    @Test
    @DisplayName("add attribute | ok")
    void addAttributeOk() {
        final String value = "a-value";
        final NGSIJson json = new NGSIJson();

        json.addAttribute(NGSITestFields.A_ATTRIBUTE, value);

        final JSONObject attribute = json.getJSONObject(NGSITestFields.A_ATTRIBUTE.label());

        assertAll("Expected attribute",
                () -> assertEquals(NGSITestFields.A_ATTRIBUTE.type().label(), attribute.getString(NGSICommonFields.TYPE.label())),
                () -> assertEquals(value, attribute.getString(NGSICommonFields.VALUE.label())));
    }

    @Test
    @DisplayName("add attribute | when value is null | should not add | ok")
    void addAttributeNullOk() {
        final NGSIJson json = new NGSIJson();

        json.addAttribute(NGSITestFields.A_ATTRIBUTE, null);

        assertFalse(json.has(NGSITestFields.A_ATTRIBUTE.label()));
    }

    @Test
    @DisplayName("get int | ok")
    void getIntOk() {
        final Integer value = 10;
        final NGSIJson json = new NGSIJson();

        json.put(NGSITestFields.B_ATTRIBUTE.label(), value);

        assertEquals(value, json.getInt(NGSITestFields.B_ATTRIBUTE));
    }

    @Test
    @DisplayName("get int | when key is not present | JsonException ")
    void getIntKeyNotPresentException() {
        final NGSIJson json = new NGSIJson();

        assertThrows(JSONException.class, () -> json.getInt(NGSITestFields.A_ATTRIBUTE));
    }

    @Test
    @DisplayName("get int | value is not an int | JsonException ")
    void getIntValueNotIntException() {
        final String value = "not-an-int";
        final NGSIJson json = new NGSIJson();

        json.put(NGSITestFields.A_ATTRIBUTE.label(), value);

        assertThrows(JSONException.class, () -> json.getInt(NGSITestFields.A_ATTRIBUTE));
    }

    @Test
    @DisplayName("get String | ok ")
    void getStringOk() {
        final String value = "a-string";
        final NGSIJson json = new NGSIJson();

        json.put(NGSITestFields.A_ATTRIBUTE.label(), value);

        assertEquals(value, json.getString(NGSITestFields.A_ATTRIBUTE));
    }

    @Test
    @DisplayName("get String | when key is not present | JsonException ")
    void getStringKeyNotPresentException() {
        final NGSIJson json = new NGSIJson();

        assertThrows(JSONException.class, () -> json.getString(NGSITestFields.A_ATTRIBUTE));
    }

    @Test
    @DisplayName("get String or null | when key is present | ok ")
    void getStringOrKeyPresentOk() {
        final String value = "a-string";
        final NGSIJson json = new NGSIJson();

        json.put(NGSITestFields.A_ATTRIBUTE.label(), value);

        assertEquals(value, json.getStringOrNull(NGSITestFields.A_ATTRIBUTE));
    }

    @Test
    @DisplayName("get String or null | when key is not present | null ")
    void getStringOrNullKeyNoPresent() {
        final NGSIJson json = new NGSIJson();

        assertNull(json.getStringOrNull(NGSITestFields.A_ATTRIBUTE));
    }
}