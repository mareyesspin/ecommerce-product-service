package com.techtrove.ecommerce.products.config;


import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import com.techtrove.ecommerce.products.models.config.AwsCredentials;
import com.techtrove.ecommerce.products.models.dto.ProductsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.Collections;

@Configuration
public class AwsConfig {

    @Autowired
    ProductsProperties productsProperties;





    @Bean
    public AwsCredentials awsCredentials() {
        return AwsCredentials.create(productsProperties.awsRegion);
    }
    @Autowired
    private Environment env;

    @Bean
    public AmazonSQS amazonSQS(){
        return AmazonSQSClientBuilder.standard()
                .withRegion(productsProperties.awsRegion)
                .withCredentials(isLocalEnvironment()
                        ? new com.amazonaws.auth.EnvironmentVariableCredentialsProvider()
                        : WebIdentityTokenCredentialsProvider.create()
                ).build();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient(AwsCredentials awsCredentials) {
        return SqsAsyncClient.builder()
                .region(awsCredentials.getRegion())
                .credentialsProvider(isLocalEnvironment() ? EnvironmentVariableCredentialsProvider.create() : WebIdentityTokenFileCredentialsProvider.create())
                .build();
    }


    @Bean
    public AmazonSQSAsync amazonSQSAsync(AwsCredentials awsCredentials) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(isLocalEnvironment()
                        ? new com.amazonaws.auth.EnvironmentVariableCredentialsProvider()
                        : WebIdentityTokenCredentialsProvider.create()
                )
                .withRegion(productsProperties.awsRegion)
                .build();
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory() {
        final QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        final MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setStrictContentTypeMatch(false);
        factory.setArgumentResolvers(Collections.singletonList(new PayloadMethodArgumentResolver(messageConverter)));
        return factory;
    }

    @Bean
    public QueueMessageHandler queueMessageHandler(AmazonSQSAsync sqsClient, QueueMessageHandlerFactory queueMessageHandlerFactory) {
        queueMessageHandlerFactory.setAmazonSqs(sqsClient);
        return queueMessageHandlerFactory.createQueueMessageHandler();
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(AmazonSQSAsync amazonSQSAsync,
                                                                         QueueMessageHandler queueMessageHandler,
                                                                         @Qualifier("threadPoolTaskExecutor") ThreadPoolTaskExecutor threadPoolExecutor) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setAmazonSqs(amazonSQSAsync);
        simpleMessageListenerContainer.setMessageHandler(queueMessageHandler);
        simpleMessageListenerContainer.setMaxNumberOfMessages(productsProperties.sqsMaxMessages);
        simpleMessageListenerContainer.setTaskExecutor(threadPoolExecutor);

        return simpleMessageListenerContainer;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(productsProperties.sqsMaxMessages);
        executor.setThreadNamePrefix("queueExec");
        executor.initialize();
        return executor;
    }




    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: ---
     * @Date: 29/06/23
     * @return boolean
     */
    private boolean isLocalEnvironment() {
        return env.getProperty("ENV") == null;
    }

}
