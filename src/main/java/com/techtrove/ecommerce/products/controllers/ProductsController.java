package com.techtrove.ecommerce.products.controllers;



import com.techtrove.ecommerce.core.interfaces.ListService;
import com.techtrove.ecommerce.core.interfaces.QueryService;
import com.techtrove.ecommerce.core.interfaces.TransaccionalService;
import com.techtrove.ecommerce.core.models.dto.ServiceModel;
import com.techtrove.ecommerce.core.models.dto.Transaction;
import com.techtrove.ecommerce.core.models.exception.ECommResourceNotFoundException;
import com.techtrove.ecommerce.products.models.request.ProductPostRequest;
import com.techtrove.ecommerce.products.models.request.ProductPutRequest;
import com.techtrove.ecommerce.products.models.response.ProductResponse;
import com.techtrove.ecommerce.products.service.ProductsListService;
import com.techtrove.ecommerce.products.service.ProductsQueryService;
import com.techtrove.ecommerce.products.service.ProductsTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
public class ProductsController {

    @Qualifier("productsTransactionService")
    private final TransaccionalService productsTransactionService;

    @Qualifier("productsQueryService")
    private final QueryService productsQueryService;
    @Qualifier("productsListService")
    private final ListService productsListService;


    @Autowired
    public ProductsController(TransaccionalService productsTransactionService,
                              QueryService productsQueryService,
                              ListService productsListService) {

        this.productsTransactionService = productsTransactionService;
        this.productsQueryService = productsQueryService;
        this.productsListService = productsListService;
    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: EndPoint para registar un producto
     * @Date: 12/10/23
     * @param productPostRequest
     * @return ResponseEntity<Transaction>
     */
    @PostMapping(value = "products",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> productRegister(@RequestBody ProductPostRequest productPostRequest){

        Optional<Transaction> oTransaction;
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setModel(productPostRequest);
        serviceModel.setProcessNumber(ProductsTransactionService.PROCESS.REGISTER_PRODUCTS.name());

        Transaction transaction = productsTransactionService.procesaTransaccion(serviceModel).get();
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: EndPoint para actualizar un producto
     * @Date: 12/10/23
     * @param productPutRequest
     * @param id
     * @return ResponseEntity<Transaction>
     */
    @PutMapping(value = "products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> updateProduct(@RequestBody ProductPutRequest productPutRequest, @PathVariable Long id){

        Optional<Transaction> oTransaction;
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setModel(productPutRequest);
        serviceModel.setAditionalField("productId", String.valueOf(id));
        serviceModel.setProcessNumber(ProductsTransactionService.PROCESS.UPDATE_PRODUCTS.name());

        Transaction transaction = productsTransactionService.procesaTransaccion(serviceModel).get();
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: End point para eliminar un producto
     * @Date: 12/10/23
     * @param id
     * @return ResponseEntity<Transaction>
     */

    @DeleteMapping(value = "products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> deleteProduct(@PathVariable Long id){

        Optional<Transaction> oTransaction;
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setAditionalField("productId", String.valueOf(id));
        serviceModel.setProcessNumber(ProductsTransactionService.PROCESS.DELETE_PRODUCTS.name());

        Transaction transaction = productsTransactionService.procesaTransaccion(serviceModel).get();
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: EndPoint para obtener un producto en especifico
     * @Date: 12/10/23
     * @param id
     * @return ResponseEntity<Transaction>
     */
    @GetMapping(value = "products/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id){

        ProductResponse productResponse;
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setAditionalField("productId", String.valueOf(id));
        serviceModel.setQueryNumber(ProductsQueryService.QUERYS.GET_PRODUCT_BY_ID.name());
        productResponse = (ProductResponse)productsQueryService.query(serviceModel)
                .orElseThrow(()-> new ECommResourceNotFoundException("El producto con id["+ id + "] no existe"));
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    /**
     *
     * @Author: Miguel A.R.S.
     * @Email: miguel.reyes@spinbyoxxo.com.mx
     * @Description: EndPoint para obtener una lista de productos
     * @Date: 12/10/23
     * @return ResponseEntity<List<ProductResponse>>
     */

    @GetMapping(value = "products",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> getAllProducts(){

        List<ProductResponse> productResponseList;
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setListNumber(ProductsListService.LIST.GET_ALL_PRODUCTS.name());
        productResponseList =(List<ProductResponse>) productsListService.list(serviceModel);

        return new ResponseEntity<>(productResponseList, HttpStatus.OK);
    }













}
