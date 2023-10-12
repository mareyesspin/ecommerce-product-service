package com.techtrove.ecommerce.products.repository;

import com.techtrove.ecommerce.products.models.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    List<ProductEntity> findAll();
}
