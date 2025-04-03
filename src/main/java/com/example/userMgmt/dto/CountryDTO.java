package com.example.userMgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private Long id;
    private String commonName;
    private String officialName;
    private String capital;
    private String region;
    private String languages;
    private String currencies;
    private Long population;
    private String flags;
}