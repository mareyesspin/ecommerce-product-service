package com.techtrove.ecommerce.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.techtrove.ecommerce.core.utils.DatesConverter.TIME_ZONE_DEFAULT;


@SpringBootApplication
@ComponentScan(basePackages = {"com.techtrove"})
public class ProductsServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ProductsServiceApp.class, args);
    }
    @PostConstruct
    public void init(){
        // Setting Spring Boot  SetTimeZone.
        TimeZone tz = TimeZone.getTimeZone(TIME_ZONE_DEFAULT);
        TimeZone.setDefault(tz);
    }

}
