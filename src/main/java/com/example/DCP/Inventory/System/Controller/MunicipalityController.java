package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.MunicipalityEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.MunicipalityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/municipality")
public class MunicipalityController {
    private final MunicipalityService municipalityService;

    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllMunicipalities() {
        List<MunicipalityEntity> municipalities = municipalityService.getAllMunicipalities();
        return Response.response(HttpStatus.OK, "Municipalities found", municipalities);
    }

    // Get municipality by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getMunicipalityById(@PathVariable Long id) {
        MunicipalityEntity municipality = municipalityService.getMunicipalityById(id);
        return Response.response(HttpStatus.OK, "Municipality found", municipality);
    }

    // Create a new municipality
    @PostMapping("/create")
    public ResponseEntity<Object> createMunicipality(@RequestBody MunicipalityEntity municipality) {
        MunicipalityEntity createdMunicipality = municipalityService.saveMunicipality(municipality);
        return Response.response(HttpStatus.CREATED, "Municipality created successfully", createdMunicipality);
    }

    // Delete municipality by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMunicipality(@PathVariable Long id) {
        municipalityService.deleteMunicipality(id);
        return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "Municipality deleted successfully");
    }

    // Save all municipalities
    @PostMapping("/create_all")
    public ResponseEntity<Object> addMunicipalities(@RequestBody List<MunicipalityEntity> municipalities) {
        try {
            municipalityService.saveAll(municipalities);
            return ResponseEntity.status(HttpStatus.CREATED).body("Municipalities added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add municipalities");
        }
    }
}
