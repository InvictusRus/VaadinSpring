package com.example.VAADINSPRINGFINAL.repository;

import com.example.VAADINSPRINGFINAL.Entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
/**
 * Интерфейс для работы с БД
 */
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    List<Customer> findAll();
}