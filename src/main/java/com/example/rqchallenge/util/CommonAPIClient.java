/*
 *
 */
package com.example.rqchallenge.util;

import com.example.rqchallenge.config.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Data
@Slf4j
public class CommonAPIClient {

    OkHttpClient client;

    @Autowired
    public CommonAPIClient() {
        this.client = new OkHttpClient.Builder().addInterceptor(new OkHttpInterceptor()).build();
    }

    /**
     *
     * @param requestUrl
     * @param method
     * @param requestBody
     * @return Optional response, received from dummy API
     */
    public Optional<String> getResponseFromDummyAPI(final String requestUrl, final String method, final Optional<String> requestBody) throws Exception {
        String responseData = null;
        Request request = null;
        log.debug("Requesting data from dummyAPI URL:%s, Method:%s", requestUrl, method);

        //Prepare request object based on method type
        switch (method.toUpperCase()){
            case "GET":
                request = new Request.Builder().url(requestUrl).build();
                break;
            case "POST":
                if(requestBody.isPresent())request = new Request.Builder().url(requestUrl)
                        .post(RequestBody.create(requestBody.get(),Constants.JSON)).build();
                break;
            case "PUT":
                if(requestBody.isPresent())request = new Request.Builder().url(requestUrl)
                        .put(RequestBody.create(requestBody.get(),Constants.JSON)).build();
                break;
            case "DELETE":
                request = new Request.Builder().url(requestUrl).delete().build();
                break;
            default:
                log.error("Invalid method type, %s", method);
                request = null;
                break;
        }

        //if request object is still null
        if(Objects.isNull(request)){
            log.error("Unable to prepare request URL:%s, Method:%s", requestUrl, method);
            return Optional.empty();
        }

        try (Response response = this.client.newCall(request).execute()) {
             responseData = response.body().string();
            log.debug("Received response from dummyAPI URL:%s , Response:%s ", requestUrl, response.code());
        } catch (IOException e) {
            log.error(String.format("Error occurred while retrieving data from URL:%s, Method:%s", requestUrl, method), e);
            throw new Exception(String.format("Unable to process request URL %s, Method:%s", requestUrl, method));
        }
        return Optional.of(responseData);
    }
}
