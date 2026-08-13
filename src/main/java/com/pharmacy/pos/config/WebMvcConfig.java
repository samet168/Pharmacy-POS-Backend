package com.pharmacy.pos.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Web MVC configuration to support application/octet-stream content type
 * for JSON payloads in multipart requests from Swagger UI.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Find the MappingJackson2HttpMessageConverter and add support for application/octet-stream
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
                // Create a new list with the existing media types plus application/octet-stream
                List<MediaType> mediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
                mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
                jsonConverter.setSupportedMediaTypes(mediaTypes);
                break;
            }
        }
    }
}
