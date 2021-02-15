package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.GoogleVO;

@Repository
public interface GoogleRepository extends JpaRepository <GoogleVO,Long>{

    Optional<GoogleVO> findByEmail(String email);

    Boolean existsByEmail(String email);
    
    void deleteById(String email);
}
