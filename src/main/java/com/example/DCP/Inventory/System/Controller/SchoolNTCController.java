package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.SchoolNTCEntity;
import com.example.DCP.Inventory.System.Service.SchoolNTCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/school_ntc")
public class SchoolNTCController {

    private final SchoolNTCService schoolNTCService;

    @Autowired
    public SchoolNTCController(SchoolNTCService schoolNTCService) {
        this.schoolNTCService = schoolNTCService;
    }

    @PostMapping("/create")
    public ResponseEntity<SchoolNTCEntity> createSchoolNTC(@RequestBody SchoolNTCEntity schoolNTC) {
        SchoolNTCEntity createdSchoolNTC = schoolNTCService.createSchoolNTC(schoolNTC);
        return ResponseEntity.ok(createdSchoolNTC);
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<SchoolNTCEntity>> getAllSchoolNTCs() {
        List<SchoolNTCEntity> schoolNTCs = schoolNTCService.getAllSchoolNTCs();
        return ResponseEntity.ok(schoolNTCs);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SchoolNTCEntity> getSchoolNTCById(@PathVariable Long id) {
        Optional<SchoolNTCEntity> schoolNTC = schoolNTCService.getSchoolNTCById(id);
        return schoolNTC.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SchoolNTCEntity> updateSchoolNTC(@PathVariable Long id, @RequestBody SchoolNTCEntity updatedNTC) {
        try {
            SchoolNTCEntity updatedSchoolNTC = schoolNTCService.updateSchoolNTC(id, updatedNTC);
            return ResponseEntity.ok(updatedSchoolNTC);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSchoolNTC(@PathVariable Long id) {
        try {
            schoolNTCService.deleteSchoolNTC(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
