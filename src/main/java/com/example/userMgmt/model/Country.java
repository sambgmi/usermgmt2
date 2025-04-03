package com.example.userMgmt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commonName;
    private String officialName;
    private String capital;
    private String region;
    
    @Column(columnDefinition = "TEXT")
    private String languages;
    
    @Column(columnDefinition = "TEXT")
    private String currencies;
    
    private Long population;
    private String flags;
}