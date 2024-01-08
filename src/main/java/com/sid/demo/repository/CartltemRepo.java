package com.sid.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sid.demo.models.Cartltem;

@Repository
public interface CartltemRepo extends JpaRepository<Cartltem, Long>{

}
