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
        /* try the request */
        Response response = chain.proceed(request);
        int tryCount = 0;
        while (response.code() != 200 && tryCount < 10) {
            response.close();
            log.info(String.format("Intercept Request is not successful - %d", tryCount));
            tryCount++;
            // retry the request
            Request newRequest = request.newBuilder().build();
            response = chain.proceed(newRequest);
        }

        // otherwise just pass the original response on
        return response;
    }
}
