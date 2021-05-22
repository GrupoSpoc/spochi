package com.spochi.service.fiware.ngsi;

public interface NGSISerializable {
    NGSIJson toNGSIJson(String id);
}
