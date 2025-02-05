package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.MunicipalityEntity;
import com.example.DCP.Inventory.System.Repository.MunicipalityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipalityService {
    private final MunicipalityRepository municipalityRepository;

    public MunicipalityService(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    public List<MunicipalityEntity> getAllMunicipalities() {
        return municipalityRepository.findAll();
    }

    public MunicipalityEntity getMunicipalityById(Long id) {
        return municipalityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Municipality not found"));
    }

    public MunicipalityEntity saveMunicipality(MunicipalityEntity municipality) {
        return municipalityRepository.save(municipality);
    }

    public void deleteMunicipality(Long id) {
        municipalityRepository.deleteById(id);
    }

    public void saveAll(List<MunicipalityEntity> municipalities) {
        municipalityRepository.saveAll(municipalities);
    }

}
