package com.techtrove.ecommerce.products.models.entity;


import com.techtrove.ecommerce.core.models.entity.EntityMasterAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="inventory")
public class InventoryEntity extends EntityMasterAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventori_id")
    private Long inventoriId;

    @Column(name = "product_id")
    private Long productId;
    @Column(name = "stock")
    private Long stock;

}
