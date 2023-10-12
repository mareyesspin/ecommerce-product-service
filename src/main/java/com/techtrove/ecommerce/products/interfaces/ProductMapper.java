package com.techtrove.ecommerce.products.interfaces;


import com.techtrove.ecommerce.products.models.entity.InventoryEntity;
import com.techtrove.ecommerce.products.models.entity.ProductEntity;
import com.techtrove.ecommerce.products.models.request.ProductPostRequest;
import com.techtrove.ecommerce.products.models.request.ProductPutRequest;
import com.techtrove.ecommerce.products.models.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {

    ProductEntity toProductEntity(ProductPostRequest productPostRequest);


    InventoryEntity toInventoryEntity(ProductPostRequest productPostRequest);

    ProductEntity toProductEntity(ProductPutRequest productPutRequest);

    ProductResponse toProductResponse(ProductEntity productEntity);


}
