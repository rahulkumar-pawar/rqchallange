/*
 * OkHttpInterceptor is interceptor for retry mechanism.
 * If http status is not 200 it will attempt the request again.
 * By default, it attempts 10 time, but later it can be changed based on requirements
 */
package com.example.rqchallenge.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class OkHttpInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int retryCount = 0;
        // Retry 10 times if status is not 200
        while (response.code() != 200 && retryCount < 10) {
            response.close(); //Until this response doesn't get closed, we cannot make another request
            log.info(String.format("Intercept Request is not successful - %d", retryCount));
            retryCount++;
            Request newRequest = request.newBuilder().build();
            response = chain.proceed(newRequest);
        }

        // Otherwise, just pass the original response on
        return response;
    }
}
