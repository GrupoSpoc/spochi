package com.spochi.repository.fiware.rest;

import com.spochi.util.AssertUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestPerformerTest {
    private static final String URL = "http://url";


    @Test
    @DisplayName("get | ok")
    void getOk() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        when(restClient.execute(any())).thenReturn(new FiwareResponse("1", Collections.emptyMap()));

        assertEquals(Integer.valueOf(1), performer.get(URL, Integer::parseInt));
    }

    @Test
    @DisplayName("get | RestException | status not found 404 | should return null | ok")
    void getNotFound() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        when(restClient.execute(any())).thenThrow(new RestException("not-found", HttpStatus.SC_NOT_FOUND));

        assertNull(performer.get(URL, r -> r));
    }

    @Test
    @DisplayName("get | RestException | no status | should throw RestException")
    void getRestException() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        when(restClient.execute(any())).thenThrow(new RestException("test-error"));

        AssertUtils.assertException(RestException.class, () -> performer.get(URL, r -> r), "test-error");
    }

    @Test
    @DisplayName("get | RuntimeException |  should throw RestException")
    void getRuntimeException() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        when(restClient.execute(any())).thenThrow(new RuntimeException("test-error"));

        AssertUtils.assertException(RestException.class, () -> performer.get(URL, r -> r), "test-error");
    }


    @Test
    @DisplayName("post json | ok")
    void postJson() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        when(restClient.execute(any())).thenReturn(new FiwareResponse("ok", Collections.emptyMap()));

        assertDoesNotThrow(() -> performer.postJson(URL, "payload"));
    }

    @Test
    @DisplayName("get | given exception | should throw RestException")
    void postJsonException() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        when(restClient.execute(any())).thenThrow(new RestException("test-error"));

        AssertUtils.assertException(RestException.class, () -> performer.postJson(URL, "payload"), "test-error");
    }

    @Test
    @DisplayName("count | ok")
    void count() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Fiware-Total-Count", Collections.singletonList("5"));

        when(restClient.execute(any())).thenReturn(new FiwareResponse("ok", headers));

        assertEquals(5, performer.count(URL));
    }

    @Test
    @DisplayName("count | given exception | should throw RestException")
    void countException() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        when(restClient.execute(any())).thenThrow(new RestException("test-error"));

        AssertUtils.assertException(RestException.class, () -> performer.count(URL), "test-error");
    }

    @Test
    @DisplayName("count | given response without header | should throw RestException")
    void countNoHeaderException() throws IOException {
        final RestClient restClient = mock(RestClient.class);
        final RestPerformer performer = new RestPerformer(restClient);
        when(restClient.execute(any())).thenReturn(new FiwareResponse("1", Collections.emptyMap()));

        assertThrows(RestException.class, () -> performer.count(URL));
    }

    @Test
    @DisplayName("count | given response with header invalid value | should throw RestException")
    void countHeaderInvalidValueException() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Fiware-Total-Count", Collections.singletonList("not-a-number"));

        final RestPerformer performer = new RestPerformerForTest(() -> new FiwareResponse("1", headers));

        assertThrows(RestException.class, () -> performer.count(URL));
    }

}