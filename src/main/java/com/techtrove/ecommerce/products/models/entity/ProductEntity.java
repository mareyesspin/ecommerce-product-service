package com.techtrove.ecommerce.products.models.entity;

import com.techtrove.ecommerce.core.models.entity.EntityMasterAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="product")
public class ProductEntity extends EntityMasterAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productID;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_sku")
    private String productSKU;
    @Column(name = "product_details")
    private String productDetails;
    @Column(name = "category_id")
    private Integer categoryId;
    @Column(name = "price_product")
    private BigDecimal priceProduct;
}
