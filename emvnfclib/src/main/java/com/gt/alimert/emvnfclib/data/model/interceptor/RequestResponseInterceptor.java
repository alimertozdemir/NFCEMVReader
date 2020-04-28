package com.gt.alimert.emvnfclib.data.model.interceptor;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author AliMertOzdemir
 * @class RequestResponseInterceptor
 * @created 28.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class RequestResponseInterceptor implements Interceptor {

    private Map<String, String> requestHeader;

    public RequestResponseInterceptor(Map<String, String> requestHeader){
        this.requestHeader = requestHeader;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder requestBuilder = request.newBuilder();

        String headerName = "X-MPOS-Request-Header";

        requestBuilder.addHeader(headerName, requestHeader.get(headerName));

        return chain.proceed(requestBuilder.build());

    }
}

