package com.spochi.repository.fiware.ngsi;

public enum NGSICommonFields implements NGSIField {
    ID("id", NGSIFieldType.TEXT) {
        @Override
        public String prefix() {
            return "urn:ngsi-ld:";
        }
    },
    TYPE("type", NGSIFieldType.TEXT),
    VALUE("value", NGSIFieldType.TEXT);

    private final String label;
    private final NGSIFieldType type;

    NGSICommonFields(String label, NGSIFieldType type) {
        this.label = label;
        this.type = type;
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public NGSIFieldType type() {
        return this.type;
    }

    public String prefix() {
        return "";
    }
}
