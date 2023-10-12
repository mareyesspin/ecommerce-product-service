package com.techtrove.ecommerce.products.repository;

import com.techtrove.ecommerce.products.models.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity,Long> {

}
