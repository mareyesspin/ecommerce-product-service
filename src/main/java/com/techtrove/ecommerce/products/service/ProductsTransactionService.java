package com.techtrove.ecommerce.products.service;

import com.amazonaws.services.sqs.AmazonSQS;

import com.techtrove.ecommerce.core.interfaces.TransaccionalService;
import com.techtrove.ecommerce.core.models.dto.ServiceModel;
import com.techtrove.ecommerce.core.models.dto.Transaction;
import com.techtrove.ecommerce.core.models.exception.ECommBadRequestException;
import com.techtrove.ecommerce.core.models.exception.ECommConflictException;
import com.techtrove.ecommerce.core.models.exception.ECommResourceNotFoundException;
import com.techtrove.ecommerce.products.interfaces.ProductMapper;
import com.techtrove.ecommerce.products.models.dto.ProductsProperties;

import com.techtrove.ecommerce.products.models.entity.InventoryEntity;
import com.techtrove.ecommerce.products.models.entity.ProductEntity;
import com.techtrove.ecommerce.products.models.request.ProductPostRequest;
import com.techtrove.ecommerce.products.models.request.ProductPutRequest;
import com.techtrove.ecommerce.products.repository.InventoryRepository;
import com.techtrove.ecommerce.products.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;


@Slf4j
@Service
@Qualifier("productsTransactionService")
public class ProductsTransactionService implements TransaccionalService {

    public enum PROCESS {
        REGISTER_PRODUCTS,
        DELETE_PRODUCTS,
        UPDATE_PRODUCTS
    }

    private ProductsProperties productsProperties;
    private AmazonSQS amazonSQS;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private ProductMapper productMapper;






    /**
     * constructor speitransaction service.
     *
     * @param productsProperties  the speiTransactionProperties
     * @param amazonSQS           the amazonSQS
     * @param productRepository   the taSpeiOutEntityRepository
     * @param inventoryRepository
     * @param productMapper       the speiTransactionMapper
     */
    @Autowired
    public ProductsTransactionService(ProductsProperties productsProperties,
                                      AmazonSQS amazonSQS,
                                      ProductRepository productRepository,
                                      InventoryRepository inventoryRepository,
                                      ProductMapper productMapper) {

        this.productsProperties = productsProperties;
        this.amazonSQS = amazonSQS;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.productMapper = productMapper;


    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: metodo para procesar cualquier proceso considerado como una transaccion.
     * @Date: 12/10/23
     * @param serviceModel
     * @return Optional<Transaction>
     */
    @Override
    public Optional<Transaction> procesaTransaccion(ServiceModel serviceModel) {
        Optional<Transaction> oTransaction= null;
        ProductsTransactionService.PROCESS processNumberEnum = ProductsTransactionService.PROCESS.valueOf(serviceModel.getProcessNumber());

        switch (processNumberEnum){
            case REGISTER_PRODUCTS:
                    oTransaction = registerProducts(serviceModel);
                break;
            case UPDATE_PRODUCTS:
                oTransaction = updateProducts(serviceModel);
                break;
            case DELETE_PRODUCTS:
                oTransaction = deleteProduct(serviceModel);
                break;
            default:
                oTransaction = Optional.empty();
                break;
        }
        return oTransaction;
    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: Metodo para registrar un producto en BD.
     * @Date: 12/10/23
     * @param serviceModel
     * @return Optional<Transaction>
     */
    @Transactional
    private Optional<Transaction> registerProducts(ServiceModel serviceModel ){
        Transaction transaction;
        InventoryEntity inventoryEntity;
        ProductPostRequest productPostRequest = serviceModel.getModel(ProductPostRequest.class);
        // convertimos el requeste a un entity
        ProductEntity productEntity = productMapper.toProductEntity(productPostRequest);

        // Damos de alta el producto
         productRepository.save(productEntity);

         //damos de alta el stock en el inventario
        inventoryEntity = productMapper.toInventoryEntity(productPostRequest);
        inventoryEntity.setProductId(productEntity.getProductID());
        inventoryRepository.save(inventoryEntity);

         transaction = new Transaction("productID", productEntity.getProductID());
         return Optional.of(transaction);

    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: Metodo para actualizar un producto
     * @Date: 12/10/23
     * @param serviceModel
     * @return Optional<Transaction>
     */

    private Optional<Transaction> updateProducts(ServiceModel serviceModel){
        Transaction transaction;
        ProductPutRequest productPutRequest = serviceModel.getModel(ProductPutRequest.class);
        Long productId = Long.valueOf(serviceModel.getAditionalField("productId"));
        // se busca el producto que se va actualizar
        productRepository.findById(productId)
                .map((productEntity) ->{
                    // actualizamos el producto

                    //productEntity = productMapper.toProductEntity(productPutRequest);
                    productEntity.setPriceProduct(productPutRequest.getPriceProduct());
                    productEntity.setProductDetails(productPutRequest.getProductDetails());
                    productEntity.setProductName(productPutRequest.getProductName());
                    productEntity.setProductSKU(productPutRequest.getProductSKU());
                    productEntity.setPriceProduct(productPutRequest.getPriceProduct());
                    productRepository.save(productEntity);

                    // actualizamos el inventario
                    inventoryRepository.findById(productId)
                            .map((inventoryEntity)->{
                                inventoryEntity.setStock(productPutRequest.getStock());
                                inventoryRepository.save(inventoryEntity);
                                return inventoryEntity;
                            })
                            .orElseThrow( () -> new ECommBadRequestException("No fue posible actualizar el inventario"));
                    return  Optional.empty();
                })
                .orElseThrow(()-> new ECommResourceNotFoundException("El producto con id["+ productId + "] no existe"));
        transaction = new Transaction("productID", productId);
        transaction.setDetail("message", "Producto Actualizado");
        return Optional.of(transaction);
    }


    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: metodo para eliminar un producto, siempre y cuando no tenga stock
     * @Date: 12/10/23
     * @param serviceModel
     * @return Optional<Transaction>
     */
    private Optional<Transaction> deleteProduct(ServiceModel serviceModel){
        Transaction transaction;
        Long productID = Long.valueOf( serviceModel.getAditionalField("productId"));

        ProductEntity p = productRepository.findById(productID)
                .map((productEntity) ->{
                    // se valida que no halla stock para poder eliminarlo
                    inventoryRepository.findById(productEntity.getProductID())
                            .ifPresent((inventoryEntity)->{
                                // si aun tiene stock, se marca una excepcion
                                if( inventoryEntity.getStock() > 0) {
                                    log.info("***** stock {}",inventoryEntity.getStock());
                                    throw new ECommConflictException("No se puede eliminar el producto, por que aun tiene stock");
                                }
                                else {
                                    // se elimina del inventario
                                    inventoryRepository.delete(inventoryEntity);
                                    log.info("producto eliminado del inventario..");
                                }
                            });

                    return  productEntity;
                }).orElseThrow(()-> new ECommResourceNotFoundException("El producto con id["+ productID + "] no existe"));
        //Eliminamos el producto del catalogo
        productRepository.delete(p);

        transaction = new Transaction("message", "El producto con id["+productID+"] fue eliminado");
        return Optional.of(transaction);
    }


}

