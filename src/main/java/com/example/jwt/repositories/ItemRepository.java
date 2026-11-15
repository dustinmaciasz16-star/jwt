package com.example.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwt.entidades.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{

}
