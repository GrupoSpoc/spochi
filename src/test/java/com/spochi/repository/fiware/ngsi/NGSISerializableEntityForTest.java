package com.spochi.repository.fiware.ngsi;

import lombok.Getter;
import lombok.Setter;

@Getter
public class NGSISerializableEntityForTest implements NGSISerializable {
    public static final NGSIEntityType NGSIType = () -> "TestEntity";

    @Setter private String id;
    private final String field1;
    private final int field2;

    public NGSISerializableEntityForTest(String field1, int field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public static NGSISerializableEntityForTest fromNGSIJson(NGSIJson json) {
        final NGSISerializableEntityForTest entity = new NGSISerializableEntityForTest(json.getString("field1"), json.getInt("field2"));
        entity.setId(json.getId());
        return entity;
    }

    @Override
    public NGSIJson toNGSIJson(String id) {
        return (NGSIJson) new NGSIJson()
                .setId(id)
                .setType(NGSIType)
                .put("field1", field1)
                .put("field2", field2);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NGSISerializableEntityForTest)) return false;
        final NGSISerializableEntityForTest other = (NGSISerializableEntityForTest) obj;
        return this.field1.equals(other.field1) && this.field2 == other.field2;
    }
}
