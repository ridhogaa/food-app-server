package org.ergea.foodapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.ergea.foodapp.dto.OrderDetailRequest;
import org.ergea.foodapp.dto.OrderDetailResponse;
import org.ergea.foodapp.dto.OrderRequest;
import org.ergea.foodapp.dto.OrderResponse;
import org.ergea.foodapp.entity.Order;
import org.ergea.foodapp.entity.OrderDetail;
import org.ergea.foodapp.entity.Product;
import org.ergea.foodapp.entity.User;
import org.ergea.foodapp.mapper.OrderMapper;
import org.ergea.foodapp.repository.OrderDetailRepository;
import org.ergea.foodapp.repository.OrderRepository;
import org.ergea.foodapp.repository.ProductRepository;
import org.ergea.foodapp.repository.UserRepository;
import org.ergea.foodapp.service.OrderService;
import org.ergea.foodapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
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
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> findAll() {
        var response = new ArrayList<OrderResponse>();
        orderRepository.findAll().forEach(order -> {
            log.info("ORDER {}", order);
            response.add(orderMapper.toOrderResponse(order));
        });
        return response;
    }

    @Override
    public OrderResponse update(UUID id, OrderRequest request) {
        validationService.validate(request);

        log.info("REQUEST : {}", request);
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Order not found"));

        if (Objects.nonNull(request.getDestinationAddress())) {
            order.setDestinationAddress(request.getDestinationAddress());
        }

        if (Objects.nonNull(request.getIsComplete())) {
            order.setIsComplete(request.getIsComplete());
        }

        order.setUser(order.getUser());

        orderRepository.save(order);
        log.info("REQUEST : {}", orderRepository.save(order));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse delete(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Order not found"));
        orderRepository.delete(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderDetailResponse> findAllDetails(Pageable pageable) {
        return orderDetailRepository.findAll(pageable).map(orderDetail -> orderMapper.toOrderDetailResponse(orderDetail));
    }

    @Override
    public OrderDetailResponse createDetail(OrderDetailRequest request) {
        validationService.validate(request);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(request.getQuantity());
        orderDetail.setTotalPrice(request.getTotalPrice());

        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id " + request.getOrderId()));
        orderDetail.setOrder(order);

        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id " + request.getProductId()));
        orderDetail.setProduct(product);

        orderDetailRepository.save(orderDetail);

        return orderMapper.toOrderDetailResponse(orderDetail);
    }
}
