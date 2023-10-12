package com.techtrove.ecommerce.products.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ProductServiceConfig implements WebMvcConfigurer {



    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        configurer.
                parameterName("mediaType").
                defaultContentType(MediaType.APPLICATION_JSON).
                mediaType("xml", MediaType.APPLICATION_XML).
                mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }



}
