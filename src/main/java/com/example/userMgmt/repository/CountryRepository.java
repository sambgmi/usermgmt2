package com.example.userMgmt.repository;

import com.example.userMgmt.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    boolean existsByCommonName(String commonName);
}