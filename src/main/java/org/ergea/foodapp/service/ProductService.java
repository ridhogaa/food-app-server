package org.ergea.foodapp.service;

import org.ergea.foodapp.dto.ProductRequest;
import org.ergea.foodapp.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    List<ProductResponse> findAll();

    ProductResponse update(UUID id, ProductRequest request);

    ProductResponse delete(UUID id);
}
