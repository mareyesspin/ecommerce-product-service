package com.techtrove.ecommerce.products.models.request;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
public class ProductPostRequest{
    private String productName;
    private String productSKU;
    private String productDetails;
    private Integer categoryId;
    private BigDecimal priceProduct;
    private Long stock;
}
