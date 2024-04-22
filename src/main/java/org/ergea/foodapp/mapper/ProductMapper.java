package org.ergea.foodapp.mapper;

import org.ergea.foodapp.dto.ProductResponse;
import org.ergea.foodapp.entity.Product;

public final class ProductMapper {
    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
