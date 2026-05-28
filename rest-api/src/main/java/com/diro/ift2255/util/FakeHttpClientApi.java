package com.diro.ift2255.util;

import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;

public class FakeHttpClientApi extends HttpClientApi {

    public Object nextResult;

    @Override
    public <T> T get(URI uri, Class<T> type) {
        return (T) nextResult;
    }

    @Override
    public <T> T get(URI uri, TypeReference<T> typeRef) {
        return (T) nextResult;
    }
}
