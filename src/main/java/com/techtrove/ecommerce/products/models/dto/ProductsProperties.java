package com.techtrove.ecommerce.products.models.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductsProperties {

    @Value("${aws.region}")
    public String awsRegion;

    @Value("${sqs.spei-out-operation.url}")
    public String speiOutQueueUrl;

    @Value("${sqs.spei-in-operation.url}")
    public String speiInQueueUrl;

    @Value("${sqs.spei-rules-operation.url}")
    public String speiRulesQueueUrl;

    @Value("${sqs.max-messages}")
    public Integer sqsMaxMessages;








}
