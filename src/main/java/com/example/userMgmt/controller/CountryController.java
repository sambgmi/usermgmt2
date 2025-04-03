package com.example.userMgmt.controller;

import com.example.userMgmt.dto.CountryDTO;
import com.example.userMgmt.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
// Removed @SecurityRequirement annotation since it's a public API
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    @Operation(summary = "Get all countries")
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get country by ID")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new country")
    public ResponseEntity<CountryDTO> createCountry(@Valid @RequestBody CountryDTO countryDTO) {
        return ResponseEntity.ok(countryService.createCountry(countryDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a country")
    public ResponseEntity<CountryDTO> updateCountry(
            @PathVariable Long id,
            @Valid @RequestBody CountryDTO countryDTO) {
        return ResponseEntity.ok(countryService.updateCountry(id, countryDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a country")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sync")
    @Operation(summary = "Sync countries from external API")
    public ResponseEntity<String> syncCountries() {
        countryService.syncCountriesFromExternalApi();
        return ResponseEntity.ok("Countries synchronized successfully");
    }
}