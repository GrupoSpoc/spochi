package com.spochi.repository.fiware.ngsi;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NGSIJsonTest | Unit")
class NGSIJsonTest {

    @Test
    @DisplayName("set id | ok")
    void setId() {
        final String id = "test-id";

        final NGSIJson json = new NGSIJson();
        json.setId(id);

        assertEquals(id, json.getString(NGSICommonFields.ID.getName()));
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
        final String type = "test-type";

        final NGSIJson json = new NGSIJson();
        json.setType(type);

        assertEquals(type, json.getString(NGSICommonFields.TYPE.getName()));
    }

    @Test
    @DisplayName("add & get attribute | ok")
    void addAttribute() {
        final String value = "a-value";
        final NGSIJson json = new NGSIJson();

        json.addAttribute(NGSITestFields.A_ATTRIBUTE, value);

        final JSONObject attribute = json.getJSONObject(NGSITestFields.A_ATTRIBUTE.getName());

        assertAll("Expected attribute",
                () -> assertEquals(NGSITestFields.A_ATTRIBUTE.getType().getName(), attribute.getString(NGSICommonFields.TYPE.getName())),
                () -> assertEquals(value, attribute.getString(NGSICommonFields.VALUE.getName())));
    }

    @Test
    @DisplayName("get attribute | ok")
    void getAttributeOk() {
        final Integer value = 10;
        final NGSIJson json = new NGSIJson();

        json.addAttribute(NGSITestFields.B_ATTRIBUTE, value);


    }

    @Test
    @DisplayName("get attribute | when attribute is not present | NGSIJsonException")
    void getAttributeNotPresentException() {

    }

    @Test
    @DisplayName("get attribute | when attribute is not JSONObject | NGSIJsonException")
    void getAttributeNotJSONObjectException() {

    }

    @Test
    @DisplayName("get attribute | when mapping function fails | NGSIJsonException")
    void getAttributeMappingFailsException() {

    }



    @Test
    @DisplayName("get attribute value Integer | ok | should call get attribute value")
    void getAttributeValueInteger() {
    }

    @Test
    @DisplayName("get attribute value String | ok | should call get attribute value")
    void getAttributeValueString() {
    }
}