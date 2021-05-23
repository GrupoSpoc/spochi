package com.spochi.repository.fiware.rest;

import com.spochi.repository.fiware.FiwareException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RestClient {
    private static final OkHttpClient client = new OkHttpClient();

    public FiwareResponse execute(Request request) throws IOException {
        final Call call = client.newCall(request);
        final okhttp3.Response okhttp3Response = call.execute();
        if (!okhttp3Response.isSuccessful()) throw new FiwareException(okhttp3Response.message(), okhttp3Response.code());
        return new FiwareResponse(okhttp3Response.body().string(), okhttp3Response.headers().toMultimap());
    }
}
