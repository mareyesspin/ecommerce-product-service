package com.techtrove.ecommerce.products.repository;

import com.techtrove.ecommerce.products.models.entity.InventoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<InventoryEntity,Long > {

}
