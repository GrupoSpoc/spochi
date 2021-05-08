package com.spochi.service.authenticate;

import java.util.function.Supplier;

public class JwtUtilForTest extends JwtUtil {
    private final Supplier<Long> currentMillisSupplier;

    public JwtUtilForTest(Supplier<Long> currentMillisSupplier) {
        this.currentMillisSupplier = currentMillisSupplier;
    }

    public JwtUtilForTest() {
        this(System::currentTimeMillis);
    }

    @Override
    protected String getSecretKey() {
        return "secret";
    }

    @Override
    protected long currentMillis() {
        return currentMillisSupplier.get();
    }
}
