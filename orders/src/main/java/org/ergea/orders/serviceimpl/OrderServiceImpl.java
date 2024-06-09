package org.ergea.orders.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.ergea.orders.config.Config;
import org.ergea.orders.config.JwtInterceptor;
import org.ergea.orders.dto.OrderDetailRequest;
import org.ergea.orders.dto.OrderDetailResponse;
import org.ergea.orders.dto.OrderRequest;
import org.ergea.orders.dto.OrderResponse;
import org.ergea.orders.dto.base.BaseResponse;
import org.ergea.orders.entity.Order;
import org.ergea.orders.entity.OrderDetail;
import org.ergea.orders.mapper.OrderMapper;
import org.ergea.orders.repository.OrderDetailRepository;
import org.ergea.orders.repository.OrderRepository;
import org.ergea.orders.service.OrderService;
import org.ergea.orders.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private Config config;

    @Value("${BASEURL8080}")
    private String baseUrl;

    @Override
    public OrderResponse create(OrderRequest request, String jwtToken) {
        validationService.validate(request);
        log.info(jwtToken);
        Order order = new Order();
        order.setOrderTime(LocalDateTime.now());
        order.setIsComplete(request.getIsComplete());
        order.setDestinationAddress(request.getDestinationAddress());
        RestTemplate restTemplateWithJwt = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplateWithJwt.getInterceptors();
        interceptors.add(new JwtInterceptor(jwtToken));
        restTemplateWithJwt.setInterceptors(interceptors);
        String url = baseUrl + "v1/auth";
        ParameterizedTypeReference<BaseResponse<Map<String, Object>>> responseType =
                new ParameterizedTypeReference<BaseResponse<Map<String, Object>>>() {
                };

        // Make the request
        ResponseEntity<BaseResponse<Map<String, Object>>> response = restTemplateWithJwt.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("{}", response.getBody());
            order.setUserId((String) Objects.requireNonNull(response.getBody()).getData().get("id"));
            orderRepository.save(order);
            return orderMapper.toOrderResponse(order);
        }

        return null;
    }

    @Override
    public List<OrderResponse> findAll() {
        List<OrderResponse> response = new ArrayList<OrderResponse>();
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

//        order.setUser(order.getUser());

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

        Order order = orderRepository.findById(config.isValidUUID(request.getOrderId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id " + request.getOrderId()));
        orderDetail.setOrder(order);

//        Product product = productRepository.findById(config.isValidUUID(request.getProductId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id " + request.getProductId()));
//        orderDetail.setProduct(product);

        orderDetailRepository.save(orderDetail);

        return orderMapper.toOrderDetailResponse(orderDetail);
    }
}
