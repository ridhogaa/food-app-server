package org.ergea.foodapp.service;

import lombok.extern.slf4j.Slf4j;
import org.ergea.foodapp.dto.ProductRequest;
import org.ergea.foodapp.dto.ProductResponse;
import org.ergea.foodapp.entity.Merchant;
import org.ergea.foodapp.entity.Product;
import org.ergea.foodapp.mapper.ProductMapper;
import org.ergea.foodapp.repository.MerchantRepository;
import org.ergea.foodapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ValidationService validationService;

    public ProductResponse create(ProductRequest request) {
        validationService.validate(request);
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setName(request.getName());
        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found with id " + request.getMerchantId()));
        product.setMerchant(merchant);
        productRepository.save(product);
        return new ProductMapper().toProductResponse(product);
    }

    public List<ProductResponse> findAll() {
        var response = new ArrayList<ProductResponse>();
        productRepository.findAll().forEach(
                product -> {
                    log.info("PRODUCT : {}", product);
                    response.add(new ProductMapper().toProductResponse(product));
                }
        );
        return response;
    }

    public ProductResponse update(UUID id, ProductRequest request) {
        validationService.validate(request);
        log.info("REQUEST : {}", request);
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Product not found"));

        if (Objects.nonNull(request.getName())) {
            product.setName(request.getName());
        }

        if (Objects.nonNull(request.getPrice())) {
            product.setPrice(request.getPrice());
        }

        product.setMerchant(product.getMerchant());

        productRepository.save(product);

        return new ProductMapper().toProductResponse(product);
    }

    public ProductResponse delete(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Product not found"));
        productRepository.delete(product);
        return new ProductMapper().toProductResponse(product);
    }
}
