package com.example.vueback.controller;

import com.example.vueback.dto.OrderDto;
import com.example.vueback.entity.Order;
import com.example.vueback.repository.OrderRepository;
import com.example.vueback.service.JwtService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrderController {
    private final JwtService jwtService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(JwtService jwtService, OrderRepository orderRepository) {
        this.jwtService = jwtService;
        this.orderRepository = orderRepository;
    }
    @GetMapping("/api/orders")
    public ResponseEntity getOrder(
            @CookieValue(value = "token", required = false) String token
    ){
        // 토큰 유무 검사
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        List<Order> orders = orderRepository.findAll();

        return new ResponseEntity(orders,HttpStatus.OK);

    }


    @PostMapping("/api/orders")
    public ResponseEntity pushOrder(
            @RequestBody OrderDto orderDto,
            @CookieValue(value = "token", required = false) String token
    ){

        // 토큰 유무 검사
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Order newOrder = new Order();
        newOrder.setMemberId(jwtService.getId(token));
        newOrder.setName(orderDto.getName());
        newOrder.setAddress(orderDto.getAddress());
        newOrder.setPayment(orderDto.getPayment());
        newOrder.setCardNumber(orderDto.getCardNumber());
        newOrder.setItems(orderDto.getItems());

        orderRepository.save(newOrder);

        return new ResponseEntity<>(HttpStatus.OK);

    }


}
