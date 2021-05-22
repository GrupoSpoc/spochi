package com.spochi.repository.fiware.ngsi;

enum NGSITestFields implements NGSIField {
    A_ATTRIBUTE("aAttribute", NGSIFieldType.TEXT),
    B_ATTRIBUTE("bAttribute", NGSIFieldType.INTEGER);

    private final String name;
    private final NGSIFieldType type;


    NGSITestFields(String name, NGSIFieldType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public NGSIFieldType getType() {
        return type;
    }
}
