package org.ergea.orders.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.ergea.orders.dto.base.BaseResponse;
import org.ergea.orders.dto.OrderDetailRequest;
import org.ergea.orders.dto.OrderRequest;
import org.ergea.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.UUID;

@Tag(name = "Order")
@RestController
@RequestMapping("v1/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderRequest request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(BaseResponse.success(orderService.create(request, token), "Success Create Order"));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(BaseResponse.success(orderService.findAll(), "Success Get All Orders"));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody OrderRequest request) {
        return ResponseEntity.ok(BaseResponse.success(orderService.update(id, request), "Success Update Order"));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(BaseResponse.success(orderService.delete(id), "Success Delete Order"));
    }

    @GetMapping(path = "details")
    public ResponseEntity<?> findAllDetails(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(BaseResponse.success(orderService.findAllDetails(PageRequest.of(page, size)).getContent(), "Success Get All Order Details"));
    }

    @PostMapping(path = "details")
    public ResponseEntity<?> createDetail(@RequestBody OrderDetailRequest request) {
        return ResponseEntity.ok(BaseResponse.success(orderService.createDetail(request), "Success create order detail"));
    }

}
