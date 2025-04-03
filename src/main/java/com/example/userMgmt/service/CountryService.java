package com.example.userMgmt.service;

import com.example.userMgmt.dto.CountryDTO;
import com.example.userMgmt.model.Country;
import com.example.userMgmt.repository.CountryRepository;
import com.example.userMgmt.exception.CountryException;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
    
    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;
    private static final String COUNTRIES_API_URL = "https://restcountries.com/v3.1/all";

    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CountryDTO getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryException("Country not found with id: " + id));
        return mapToDTO(country);
    }

    @Transactional
    public CountryDTO createCountry(CountryDTO countryDTO) {
        if (countryRepository.existsByCommonName(countryDTO.getCommonName())) {
            throw new CountryException("Country already exists with name: " + countryDTO.getCommonName());
        }
        
        Country country = mapToEntity(countryDTO);
        Country savedCountry = countryRepository.save(country);
        return mapToDTO(savedCountry);
    }

    @Transactional
    public CountryDTO updateCountry(Long id, CountryDTO countryDTO) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryException("Country not found with id: " + id));
        
        updateCountryFields(country, countryDTO);
        Country updatedCountry = countryRepository.save(country);
        return mapToDTO(updatedCountry);
    }

    @Transactional
    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new CountryException("Country not found with id: " + id);
        }
        countryRepository.deleteById(id);
    }

    @Transactional
    public void syncCountriesFromExternalApi() {
        try {
            ResponseEntity<Object[]> response = restTemplate.getForEntity(COUNTRIES_API_URL, Object[].class);
            Object[] countries = response.getBody();

            if (countries != null) {
                for (Object countryData : countries) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> countryMap = (Map<String, Object>) countryData;
                        
                        @SuppressWarnings("unchecked")
                        Map<String, Object> name = (Map<String, Object>) countryMap.get("name");
                        
                        String commonName = (String) name.get("common");
                        Country country = countryRepository.findByCommonName(commonName)
                                .orElse(new Country());
                        
                        // Update country details
                        country.setCommonName(commonName);
                        country.setOfficialName((String) name.get("official"));
                        country.setCapital(extractCapital(countryMap));
                        country.setRegion((String) countryMap.get("region"));
                        country.setLanguages(convertMapToJson(countryMap.get("languages")));
                        country.setCurrencies(convertMapToJson(countryMap.get("currencies")));
                        country.setPopulation(((Number) countryMap.get("population")).longValue());
                        country.setFlags(extractFlagUrl(countryMap));

                        // Save or update country
                        countryRepository.save(country);
                        
                    } catch (Exception e) {
                        throw new CountryException("Error processing country: " + e.getMessage());
                    }
                }
            }
        } catch (RestClientException e) {
            throw new RestClientException("Failed to fetch data from external API: " + e.getMessage());
        }
    }

    private CountryDTO mapToDTO(Country country) {
        return CountryDTO.builder()
                .id(country.getId())
                .commonName(country.getCommonName())
                .officialName(country.getOfficialName())
                .capital(country.getCapital())
                .region(country.getRegion())
                .languages(country.getLanguages())
                .currencies(country.getCurrencies())
                .population(country.getPopulation())
                .flags(country.getFlags())
                .build();
    }

    private Country mapToEntity(CountryDTO dto) {
        return Country.builder()
                .commonName(dto.getCommonName())
                .officialName(dto.getOfficialName())
                .capital(dto.getCapital())
                .region(dto.getRegion())
                .languages(dto.getLanguages())
                .currencies(dto.getCurrencies())
                .population(dto.getPopulation())
                .flags(dto.getFlags())
                .build();
    }

    private void updateCountryFields(Country country, CountryDTO dto) {
        country.setCommonName(dto.getCommonName());
        country.setOfficialName(dto.getOfficialName());
        country.setCapital(dto.getCapital());
        country.setRegion(dto.getRegion());
        country.setLanguages(dto.getLanguages());
        country.setCurrencies(dto.getCurrencies());
        country.setPopulation(dto.getPopulation());
        country.setFlags(dto.getFlags());
    }

    private String extractCapital(Map<String, Object> countryMap) {
        @SuppressWarnings("unchecked")
        List<String> capitals = (List<String>) countryMap.get("capital");
        return capitals != null && !capitals.isEmpty() ? capitals.get(0) : "";
    }

    private String extractFlagUrl(Map<String, Object> countryMap) {
        @SuppressWarnings("unchecked")
        Map<String, Object> flags = (Map<String, Object>) countryMap.get("flags");
        return flags != null ? (String) flags.get("png") : "";
    }

    private String convertMapToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }
}