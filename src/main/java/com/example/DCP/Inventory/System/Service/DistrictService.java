package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.DistrictEntity;
import com.example.DCP.Inventory.System.Repository.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteDistrict(Long id) {
        districtRepository.deleteById(id);
    }

    public void saveAll(List<DistrictEntity> districts) {
        districtRepository.saveAll(districts);
    }
}
