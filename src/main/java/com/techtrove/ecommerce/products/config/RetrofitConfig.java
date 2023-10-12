package com.techtrove.ecommerce.products.config;


import com.techtrove.ecommerce.products.models.dto.ProductsProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RetrofitConfig{

    private ProductsProperties productsProperties;

    @Autowired
    public RetrofitConfig(ProductsProperties productsProperties) {
        this.productsProperties = productsProperties;
    }

   /* @Bean
    public SpeiSupportServiceEndPoints rbmSpeiSupportService() throws NoSuchAlgorithmException, KeyManagementException {
        RetrofitBuild<SpeiSupportServiceEndPoints> retrofitBuildModel = new RetrofitBuild<>();
        retrofitBuildModel.setApiKey(speiChannelProperties.apiKey);
        retrofitBuildModel.setBaseURL(speiChannelProperties.speiSupportServiceUrl);
        retrofitBuildModel.setRepositoryClass(SpeiSupportServiceEndPoints.class);
        return retrofitBuildModel.buildVendor();
    }

    */

}

