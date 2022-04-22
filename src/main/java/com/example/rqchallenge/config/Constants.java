/*
 * Constants
 */
package com.example.rqchallenge.config;

import okhttp3.MediaType;

public class Constants {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final int TOP_RECORDS = 10;
    public static enum HttpMethods{
        GET,
        POST,
        PUT,
        DELETE
    }
}
