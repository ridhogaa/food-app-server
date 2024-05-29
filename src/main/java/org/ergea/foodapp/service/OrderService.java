package org.ergea.foodapp.service;

import org.ergea.foodapp.dto.OrderDetailRequest;
import org.ergea.foodapp.dto.OrderDetailResponse;
import org.ergea.foodapp.dto.OrderRequest;
import org.ergea.foodapp.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse create(OrderRequest request, Principal principal);

    List<OrderResponse> findAll();

    OrderResponse update(UUID id, OrderRequest request);

    OrderResponse delete(UUID id);

    Page<OrderDetailResponse> findAllDetails(Pageable pageable);

    OrderDetailResponse createDetail(OrderDetailRequest request);
}
