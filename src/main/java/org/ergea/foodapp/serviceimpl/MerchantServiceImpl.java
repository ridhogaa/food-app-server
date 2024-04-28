package org.ergea.foodapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.ergea.foodapp.dto.MerchantRequest;
import org.ergea.foodapp.dto.MerchantResponse;
import org.ergea.foodapp.entity.Merchant;
import org.ergea.foodapp.mapper.MerchantMapper;
import org.ergea.foodapp.repository.MerchantRepository;
import org.ergea.foodapp.service.MerchantService;
import org.ergea.foodapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public MerchantResponse create(MerchantRequest request) {
        validationService.validate(request);
        Merchant merchant = new Merchant();
        var randomUUID = UUID.randomUUID();
        merchant.setId(randomUUID);
        merchant.setName(request.getName());
        merchant.setLocation(request.getLocation());
        merchant.setIsOpen(request.getIsOpen());
        merchantRepository.createQuerySP(randomUUID, request.getName(), request.getLocation(), request.getIsOpen());
        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public List<MerchantResponse> findAll(Boolean isOpen) {
        var response = new ArrayList<MerchantResponse>();
        if (isOpen != null) {
            merchantRepository.findAllByIsOpen(isOpen).forEach(
                    merchant -> response.add(merchantMapper.toMerchantResponse(merchant))
            );
        } else {
            merchantRepository.findAll().forEach(
                    merchant -> response.add(merchantMapper.toMerchantResponse(merchant))
            );
        }
        return response;
    }

    @Override
    public MerchantResponse update(UUID id, MerchantRequest request) {
        validationService.validate(request);
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Merchant not found"));
        merchant.setName(request.getName());
        merchant.setLocation(request.getLocation());
        merchant.setIsOpen(request.getIsOpen());
        merchantRepository.updateQuerySP(id, request.getName(), request.getLocation(), request.getIsOpen());
        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public MerchantResponse delete(UUID id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Merchant not found"));
        merchantRepository.deleteQuerySP(merchant.getId());
        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public MerchantResponse findById(UUID id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Merchant not found"));
        merchantRepository.findByIdQuerySP(merchant.getId());
        return merchantMapper.toMerchantResponse(merchant);
    }

    private MerchantResponse getMerchantResponse(MerchantRequest request, Merchant merchant) {
        merchant.setName(request.getName());
        merchant.setLocation(request.getLocation());
        merchant.setIsOpen(request.getIsOpen());
        merchantRepository.save(merchant);
        return merchantMapper.toMerchantResponse(merchant);
    }

}
