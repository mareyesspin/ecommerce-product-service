package com.techtrove.ecommerce.products.models.config;

import lombok.Builder;
import lombok.Getter;
import software.amazon.awssdk.regions.Region;

@Getter
@Builder
public class AwsCredentials {
    private Region region;

    public static AwsCredentials create(String region) {
        return AwsCredentials.builder()
                .region(Region.of(region))
                .build();
    }
}