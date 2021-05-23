package com.spochi.repository.fiware.rest;

import com.sun.istack.Nullable;
import okhttp3.Request;

import java.io.IOException;
import java.util.function.Supplier;

public class RestPerformerForTest extends RestPerformer {
    private final Supplier<FiwareResponse> responseSupplier;

    public RestPerformerForTest(Supplier<FiwareResponse> responseSupplier) {
        super(null);
        this.responseSupplier = responseSupplier;
    }
}
