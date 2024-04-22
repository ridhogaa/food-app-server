package org.ergea.foodapp.mapper;

import org.ergea.foodapp.dto.MerchantResponse;
import org.ergea.foodapp.entity.Merchant;

public final class MerchantMapper {
    public MerchantResponse toMerchantResponse(Merchant merchant){
        return MerchantResponse.builder()
                .id(merchant.getId().toString())
                .name(merchant.getName())
                .location(merchant.getLocation())
                .isOpen(merchant.getIsOpen())
                .build();
    }
}
