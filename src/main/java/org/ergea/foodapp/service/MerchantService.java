package org.ergea.foodapp.service;

import org.ergea.foodapp.dto.MerchantRequest;
import org.ergea.foodapp.dto.MerchantResponse;

import java.util.List;
import java.util.UUID;

public interface MerchantService {

    MerchantResponse create(MerchantRequest request);

    List<MerchantResponse> findAll(Boolean isOpen);

    MerchantResponse update(UUID id, MerchantRequest request);

    MerchantResponse delete(UUID id);

    MerchantResponse findById(UUID id);

}
