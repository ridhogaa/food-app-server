package org.ergea.foodapp.service;

import lombok.extern.slf4j.Slf4j;
import org.ergea.foodapp.dto.MerchantRequest;
import org.ergea.foodapp.dto.MerchantResponse;
import org.ergea.foodapp.entity.Merchant;
import org.ergea.foodapp.entity.User;
import org.ergea.foodapp.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ValidationService validationService;

    public MerchantResponse create(MerchantRequest request) {
        validationService.validate(request);
        Merchant merchant = new Merchant();
        return getMerchantResponse(request, merchant);
    }

    public List<MerchantResponse> findAll() {
        var response = new ArrayList<MerchantResponse>();
        merchantRepository.findAll().forEach(
                merchant -> response.add(new MerchantResponse(merchant.getId().toString(), merchant.getName(), merchant.getLocation(), merchant.getIsOpen()))
        );
        return response;
    }

    public MerchantResponse update(UUID id, MerchantRequest request) {
        validationService.validate(request);
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Merchant not found"));
        return getMerchantResponse(request, merchant);
    }

    public MerchantResponse delete(UUID id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Merchant not found"));
        merchantRepository.delete(merchant);
        return MerchantResponse.builder()
                .id(merchant.getId().toString())
                .name(merchant.getName())
                .location(merchant.getLocation())
                .isOpen(merchant.getIsOpen())
                .build();
    }

    private MerchantResponse getMerchantResponse(MerchantRequest request, Merchant merchant) {
        merchant.setName(request.getName());
        merchant.setLocation(request.getLocation());
        merchant.setIsOpen(request.getIsOpen());
        merchantRepository.save(merchant);
        return MerchantResponse.builder()
                .id(merchant.getId().toString())
                .name(request.getName())
                .location(request.getLocation())
                .isOpen(request.getIsOpen())
                .build();
    }
}
