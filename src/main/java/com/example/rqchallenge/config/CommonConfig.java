/*
 * Common config can be read from application.properties or application.yaml
 */
package com.example.rqchallenge.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class CommonConfig {
    @Value("${base.url}")
    public String baseUrl;

    @Value("${api.version}")
    public String apiVersion;

    @Value("${api.entity}")
    public String entity;
}
