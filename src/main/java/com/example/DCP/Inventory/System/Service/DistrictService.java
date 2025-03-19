package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.CoordinatorEntity;
import com.example.DCP.Inventory.System.Entity.DistrictEntity;
import com.example.DCP.Inventory.System.Repository.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DistrictService {
    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<DistrictEntity> getAllDistricts() {
        return districtRepository.findAll();
    }

    public DistrictEntity getDistrictById(Long id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found"));
    }

    public DistrictEntity saveDistrict(DistrictEntity district) {
        return districtRepository.save(district);
    }

    public DistrictEntity updateDistrict(Long id, DistrictEntity districtDetails) {
        Optional<DistrictEntity> existingDistrict = districtRepository.findById(id);
        if (existingDistrict.isPresent()) {
            DistrictEntity districtEntity = existingDistrict.get();
            districtEntity.setName(districtDetails.getName());
            districtEntity.setDivision(districtDetails.getDivision());
            // Set other fields as needed
            return districtRepository.save(districtEntity);
        } else {
            throw new RuntimeException("District not found");
        }
    }

    public void deleteDistrict(Long id) {
        districtRepository.deleteById(id);
    }

    public void saveAll(List<DistrictEntity> districts) {
        districtRepository.saveAll(districts);
    }

    public List<DistrictEntity> getDistrictByDivisionId(Long divisionId) {
        return districtRepository.findByDivision_DivisionId(divisionId);
    }
}
