package com.spochi.repository.fiware.rest;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class RestPerformer {
    final OkHttpClient client;

    public RestPerformer() {
        this.client = buildClient();
    }

    public <T> T get(String url, Function<String, T> mappingFunction) {
        final Request request = commonRequestBuilder(url).get().build();
        Call call = client.newCall(request);
        try {
            final String response = call.execute().body().string();
            return mappingFunction.apply(response);
        } catch (Exception e) {
            throw new RestException();
        }
    }

    private Request.Builder commonRequestBuilder(String url) {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        return new Request.Builder().url(httpBuilder.build());
    }

    // para poder mockear
    protected OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
