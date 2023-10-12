package com.techtrove.ecommerce.products.service;

import com.techtrove.ecommerce.core.interfaces.ListService;
import com.techtrove.ecommerce.core.models.dto.ServiceModel;
import com.techtrove.ecommerce.core.models.response.MasterQueryResponse;
import com.techtrove.ecommerce.products.interfaces.ProductMapper;
import com.techtrove.ecommerce.products.models.response.ProductResponse;
import com.techtrove.ecommerce.products.repository.InventoryRepository;
import com.techtrove.ecommerce.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Qualifier("productsListService")
public class ProductsListService implements ListService {



    public enum LIST {
        GET_ALL_PRODUCTS
    }
    private final ProductRepository productRepository;

    private final InventoryRepository inventoryRepository;
    private ProductMapper productMapper;

    public ProductsListService(ProductRepository productRepository,
                               InventoryRepository inventoryRepository,
                               ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<? extends MasterQueryResponse> list(ServiceModel serviceModel) {
        ProductsListService.LIST listEnum = ProductsListService.LIST.valueOf(serviceModel.getListNumber());

        if (listEnum == LIST.GET_ALL_PRODUCTS) {
            return getAllProducts(serviceModel);
        }
        return new ArrayList<>();
    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: metodo para obtener todos los productos registrados
     * @Date: 12/10/23
     * @param serviceModel
     * @return List<ProductResponse>
     */
    private List<ProductResponse> getAllProducts(ServiceModel serviceModel){

        return productRepository.findAll()
                .stream().map((productEntity) ->{
                    ProductResponse productResponse = productMapper.toProductResponse(productEntity);

                    // consultamos el stock
                    Long stock= inventoryRepository.findById(productEntity.getProductID()).map((inventoryEntity)->{
                        Long stock1 = inventoryEntity.getStock();
                        return stock1;
                    }).orElse(0L);
                    productResponse.setStock(stock);
                    return productResponse;
                }).collect(Collectors.toList());



    }

}
