package org.ergea.foodapp.controller;

import org.ergea.foodapp.dto.BaseResponse;
import org.ergea.foodapp.dto.MerchantRequest;
import org.ergea.foodapp.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MerchantRequest merchantRequest) {
        return ResponseEntity.ok(BaseResponse.success(merchantService.create(merchantRequest), "Success Create Merchant"));
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) Boolean isOpen,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location
    ) {
        return ResponseEntity.ok(BaseResponse.success(merchantService.findAll(isOpen, pageable, name, location), "Success Get All Merchants"));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody MerchantRequest merchantRequest) {
        return ResponseEntity.ok(BaseResponse.success(merchantService.update(id, merchantRequest), "Success Update Merchant"));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(merchantService.delete(id), "Success Delete Merchant"));
    }
}
