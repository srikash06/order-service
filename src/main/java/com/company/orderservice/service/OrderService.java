package com.company.orderservice.service;

import com.company.orderservice.model.Order;
import com.company.orderservice.repository.OrderRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    public Order getOrder(Long id) {

        return orderRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Order not found: " + id
                    )
                );
    }

    public Order createOrder(Order order) {

        if (order.getStatus() == null) {

            order.setStatus("NEW");
        }

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {

        orderRepository.deleteById(id);
    }
}