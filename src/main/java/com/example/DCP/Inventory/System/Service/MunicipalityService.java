package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.MunicipalityEntity;
import com.example.DCP.Inventory.System.Repository.MunicipalityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public MunicipalityEntity updateMunicipality(Long id, MunicipalityEntity municipalityDetails) {
        Optional<MunicipalityEntity> existingMunicipality = municipalityRepository.findById(id);
        if (existingMunicipality.isPresent()) {
            MunicipalityEntity municipalityEntity = existingMunicipality.get();
            municipalityEntity.setName(municipalityDetails.getName());
            municipalityEntity.setDivision(municipalityDetails.getDivision());
            // Set other fields as needed
            return municipalityRepository.save(municipalityEntity);
        } else {
            throw new RuntimeException("Municipality not found");
        }
    }

    public void deleteMunicipality(Long id) {
        municipalityRepository.deleteById(id);
    }

    public void saveAll(List<MunicipalityEntity> municipalities) {
        municipalityRepository.saveAll(municipalities);
    }

}
