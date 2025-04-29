package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.*;
import com.example.DCP.Inventory.System.Repository.ProviderRepository;
import com.example.DCP.Inventory.System.Repository.SchoolNTCRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchoolNTCService {

    private final SchoolNTCRepository schoolNTCRepository;
    private final ProviderRepository providerRepository;

    @Autowired
    public SchoolNTCService(SchoolNTCRepository schoolNTCRepository, ProviderRepository providerRepository) {
        this.schoolNTCRepository = schoolNTCRepository;
        this.providerRepository = providerRepository;
    }

    public SchoolNTCEntity createSchoolNTC(SchoolNTCEntity schoolNTC) {
        return schoolNTCRepository.save(schoolNTC);
    }

    public List<SchoolNTCEntity> createAllSchoolNTC(List<SchoolNTCEntity> schoolNTCs) {
        return schoolNTCRepository.saveAll(schoolNTCs);
    }

    @Transactional
    public List<SchoolNTCDTOEntity> getAllSchoolNTCs() {
        List<SchoolNTCEntity> ntcs = schoolNTCRepository.getAllWithSchool();

        return ntcs.stream().map(ntc -> {
            SchoolEntity school = ntc.getSchool();
            SchoolDTOEntity schoolDTO = (school != null)
                    ? new SchoolDTOEntity(school.getSchoolRecordId(), school.getSchoolId(), school.getDistrict(), school.getName())
                    : null;

            return new SchoolNTCDTOEntity(
                    ntc.getSchoolNTCId(),
                    ntc.getInternet(),
                    ntc.getPldt(),
                    ntc.getGlobe(),
                    ntc.getAm(),
                    ntc.getFm(),
                    ntc.getTv(),
                    ntc.getCable(),
                    ntc.getRemark(),
                    schoolDTO
            );
        }).collect(Collectors.toList());
    }

    public Optional<SchoolNTCEntity> getSchoolNTCById(Long id) {
        return schoolNTCRepository.findById(id);
    }

    @Transactional
    public SchoolNTCEntity updateSchoolNTC(Long id, SchoolNTCEntity updatedNTC) {
        Optional<SchoolNTCEntity> existingNTCOptional = schoolNTCRepository.findById(id);

        if (existingNTCOptional.isEmpty()) {
            throw new RuntimeException("School NTC ID not found: " + id);
        }

        SchoolNTCEntity existingNTC = existingNTCOptional.get();

        // **Step 1: Remove all existing providers before updating**
        providerRepository.deleteBySchoolNTC(existingNTC);

        // **Step 2: Update fields**
        existingNTC.setInternet(updatedNTC.getInternet());
        existingNTC.setPldt(updatedNTC.getPldt());
        existingNTC.setGlobe(updatedNTC.getGlobe());
        existingNTC.setAm(updatedNTC.getAm());
        existingNTC.setFm(updatedNTC.getFm());
        existingNTC.setTv(updatedNTC.getTv());
        existingNTC.setCable(updatedNTC.getCable());
        existingNTC.setRemark(updatedNTC.getRemark());

        // **Step 3: Save Updated School NTC**
        SchoolNTCEntity savedNTC = schoolNTCRepository.save(existingNTC);

        // **Step 4: Get the next available providerId**
        Long maxProviderId = providerRepository.findMaxProviderId(savedNTC.getSchoolNTCId());
        Long nextProviderId = (maxProviderId == null) ? 1 : maxProviderId + 1;

        // **Step 5: Add New Providers**
        List<ProviderEntity> newProviders = new ArrayList<>();
        for (ProviderEntity provider : updatedNTC.getProviders()) {
            ProviderIdEntity providerIdEntity = new ProviderIdEntity();
            providerIdEntity.setProviderId(nextProviderId++);
            providerIdEntity.setSchoolNTCId(savedNTC.getSchoolNTCId());

            ProviderEntity newProvider = new ProviderEntity();
            newProvider.setId(providerIdEntity);
            newProvider.setSchoolNTC(savedNTC);
            newProvider.setName(provider.getName());
            newProvider.setSpeed(provider.getSpeed());
            newProvider.setUnit(provider.getUnit());

            newProviders.add(newProvider);
        }

        // **Step 6: Save All New Providers**
        providerRepository.saveAll(newProviders);

        return savedNTC;
    }


    public void deleteSchoolNTC(Long id) {
        if (schoolNTCRepository.existsById(id)) {
            schoolNTCRepository.deleteById(id);
        } else {
            throw new RuntimeException("School NTC ID not found: " + id);
        }
    }

    public SchoolNTCEntity getSchoolNTCBySchoolRecordId(Long schoolRecordId) {
        return schoolNTCRepository.findBySchool_SchoolRecordId(schoolRecordId);
    }
}
