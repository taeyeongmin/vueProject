package com.example.vueback.repository;

import com.example.vueback.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {


}
