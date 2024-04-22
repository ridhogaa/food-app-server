package org.ergea.foodapp.service;

import lombok.extern.slf4j.Slf4j;
import org.ergea.foodapp.dto.OrderRequest;
import org.ergea.foodapp.dto.OrderResponse;
import org.ergea.foodapp.entity.Order;
import org.ergea.foodapp.entity.User;
import org.ergea.foodapp.repository.OrderRepository;
import org.ergea.foodapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public OrderResponse create(OrderRequest request) {
        validationService.validate(request);
        Order order = new Order();
        order.setOrderTime(LocalDateTime.now());
        order.setIsComplete(request.getIsComplete());
        order.setDestinationAddress(request.getDestinationAddress());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + request.getUserId()));
        order.setUser(user);
        orderRepository.save(order);
        return OrderResponse.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .destinationAddress(order.getDestinationAddress())
                .isComplete(order.getIsComplete())
                .build();
    }

    public List<OrderResponse> findAll() {
        var response = new ArrayList<OrderResponse>();
        orderRepository.findAll().forEach(order -> {
            log.info("ORDER {}", order);
            response.add(new OrderResponse(order.getId(), order.getOrderTime(), order.getDestinationAddress(), order.getIsComplete()));
        });
        return response;
    }

    public OrderResponse update(UUID id, OrderRequest request) {
        validationService.validate(request);

        log.info("REQUEST : {}", request);
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Order not found"));

        if (Objects.nonNull(request.getDestinationAddress())) {
            order.setDestinationAddress(request.getDestinationAddress());
        }

        if (Objects.nonNull(request.getIsComplete())) {
            order.setIsComplete(request.getIsComplete());
        }

        order.setUser(order.getUser());

        orderRepository.save(order);

        return OrderResponse.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .destinationAddress(order.getDestinationAddress())
                .isComplete(order.getIsComplete())
                .build();
    }

    public OrderResponse delete(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID Order not found"));
        orderRepository.delete(order);
        return OrderResponse.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .destinationAddress(order.getDestinationAddress())
                .isComplete(order.getIsComplete())
                .build();
    }
}