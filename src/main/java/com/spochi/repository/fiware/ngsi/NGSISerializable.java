package com.spochi.repository.fiware.ngsi;

public interface NGSISerializable {
    NGSIJson toNGSIJson(String id);
}
