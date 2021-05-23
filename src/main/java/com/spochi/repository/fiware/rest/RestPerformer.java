package com.spochi.repository.fiware.rest;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RestPerformer {
    private final RestClient client;

    @Autowired
    public RestPerformer(RestClient client) {
        this.client = client;
    }

    public <T> T get(String url, Function<String, T> mappingFunction) {
        final Request request = commonRequestBuilder(url).get().build();
        try {
            final FiwareResponse response = client.execute(request);
            return mappingFunction.apply(response.getBody());
        } catch (RestException e) {
            if (e.getCode() != null && e.getCode() == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RestException(e.getMessage());
        }
    }

    public void postJson(String url, String payload) {
        final Request request = commonRequestBuilder(url).post(RequestBody.create(payload, MediaType.parse("application/json; charset=utf-8"))).build();
        try {
            final FiwareResponse response = client.execute(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RestException(e.getMessage());
        }
    }

    public int count(String url) {
        final Request request = commonRequestBuilder(url).get().build();
        try {
            final FiwareResponse response = client.execute(request);
            return Integer.parseInt(response.getHeaders().get("Fiware-Total-Count").get(0));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RestException(e.getMessage());
        }
    }

    private Request.Builder commonRequestBuilder(String url) {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        return new Request.Builder().url(httpBuilder.build());
    }
}
