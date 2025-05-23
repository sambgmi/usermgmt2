package com.example.userMgmt.repository;

import com.example.userMgmt.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByCommonName(String commonName);
    boolean existsByCommonName(String commonName);
}