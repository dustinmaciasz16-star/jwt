package com.example.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.jwt.entidades.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

}
