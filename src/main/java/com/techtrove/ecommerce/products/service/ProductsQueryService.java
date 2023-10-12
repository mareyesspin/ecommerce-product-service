package com.techtrove.ecommerce.products.service;


import com.techtrove.ecommerce.core.interfaces.QueryService;
import com.techtrove.ecommerce.core.models.dto.ServiceModel;
import com.techtrove.ecommerce.core.models.exception.ECommConflictException;
import com.techtrove.ecommerce.core.models.exception.ECommResourceNotFoundException;
import com.techtrove.ecommerce.core.models.response.MasterQueryResponse;

import com.techtrove.ecommerce.products.interfaces.ProductMapper;
import com.techtrove.ecommerce.products.models.response.ProductResponse;
import com.techtrove.ecommerce.products.repository.InventoryRepository;
import com.techtrove.ecommerce.products.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@Qualifier("productsQueryService")
public class ProductsQueryService implements QueryService {

    public enum QUERYS {
        GET_PRODUCT_BY_ID,
    }

    private final ProductRepository productRepository;

    private final InventoryRepository inventoryRepository;
    private ProductMapper productMapper;

    public ProductsQueryService(ProductRepository productRepository,
                                InventoryRepository inventoryRepository,
                                ProductMapper productMapper) {

        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Optional<? extends MasterQueryResponse> query(ServiceModel serviceModel) {

        ProductsQueryService.QUERYS querysEnum = ProductsQueryService.QUERYS.valueOf(serviceModel.getQueryNumber());

        if (querysEnum == QUERYS.GET_PRODUCT_BY_ID) {
            return Optional.of(getProduct(serviceModel));
        }
        return Optional.empty();
    }


    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: metodo para consultar un producto por id
     * @Date: 12/10/23
     * @param serviceModel
     * @return Optional<ProductResponse>
     */
    private ProductResponse getProduct(ServiceModel serviceModel){

        Long productId = Long.valueOf(serviceModel.getAditionalField("productId"));
        ProductResponse p = productRepository.findById(productId)
                .map((productEntity) ->{
                    ProductResponse productResponse = productMapper.toProductResponse(productEntity);
                    // se consulta el stock
                    Long stock = inventoryRepository.findById(productEntity.getProductID())
                            .map((inventoryEntity)->{
                                // se obtiene el stock del producto
                                return inventoryEntity.getStock();
                            }).orElse(Long.valueOf(0));
                    productResponse.setStock(stock);

                    return  productResponse;
                }).orElseThrow(()-> new ECommResourceNotFoundException("El producto con id["+ productId + "] no existe"));
        return p;

    }


}
