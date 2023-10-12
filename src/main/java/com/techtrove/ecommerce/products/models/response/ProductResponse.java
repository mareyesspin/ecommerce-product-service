package com.techtrove.ecommerce.products.models.response;

import com.techtrove.ecommerce.core.models.response.MasterQueryResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper=false)
public class ProductResponse extends MasterQueryResponse {

    private Long productID;
    private String productName;
    private String productSKU;
    private String productDetails;
    private Integer categoryId;
    private BigDecimal priceProduct;
    private Long stock;

}
