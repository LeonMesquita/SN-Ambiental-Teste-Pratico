package com.api.company_manager.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.company_manager.dtos.CompanyDto;
import com.api.company_manager.services.CompanyService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService service;

    @PostMapping
    public ResponseEntity<Object> createCompany(@RequestBody @Valid CompanyDto companyDto) {
        String conflictError = service.checkCompanyByUniqueFields(companyDto);
        if (conflictError != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictError);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(companyDto));
    }
}
