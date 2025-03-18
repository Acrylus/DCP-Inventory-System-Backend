package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Entity.SchoolBatchListEntity;
import com.example.DCP.Inventory.System.Repository.SchoolBatchListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolBatchListService {
    private final SchoolBatchListRepository schoolBatchListRepository;

    public SchoolBatchListService(SchoolBatchListRepository schoolBatchListRepository) {
        this.schoolBatchListRepository = schoolBatchListRepository;
    }

    public List<SchoolBatchListEntity> getAllSchoolBatchLists() {
        return schoolBatchListRepository.findAll();
    }

    public SchoolBatchListEntity getSchoolBatchListById(Long id) {
        return schoolBatchListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch School List not found"));
    }

    public SchoolBatchListEntity updateSchoolBatchList(Long id, SchoolBatchListEntity updatedBatchList) {
        Optional<SchoolBatchListEntity> existingBatchListOptional = schoolBatchListRepository.findById(id);

        if (existingBatchListOptional.isEmpty()) {
            throw new RuntimeException("School Batch List not found with id: " + id);
        }

        SchoolBatchListEntity existingBatchList = existingBatchListOptional.get();

        existingBatchList.setBatch(updatedBatchList.getBatch());
        existingBatchList.setSchool(updatedBatchList.getSchool());
        existingBatchList.setDeliveryDate(updatedBatchList.getDeliveryDate());
        existingBatchList.setNumberOfPackage(updatedBatchList.getNumberOfPackage());
        existingBatchList.setStatus(updatedBatchList.getStatus());
        existingBatchList.setKeyStage(updatedBatchList.getKeyStage());
        existingBatchList.setRemarks(updatedBatchList.getRemarks());
        existingBatchList.setAccountable(updatedBatchList.getAccountable());
        existingBatchList.setPackages(updatedBatchList.getPackages());
        return schoolBatchListRepository.save(existingBatchList);
    }

    public SchoolBatchListEntity saveSchoolBatchList(SchoolBatchListEntity batchSchoolList) {
        return schoolBatchListRepository.save(batchSchoolList);
    }

    public void deleteSchoolBatchList(Long id) {
        schoolBatchListRepository.deleteById(id);
    }
    
    public void saveAll(List<SchoolBatchListEntity> schoolBatchLists) {
        schoolBatchListRepository.saveAll(schoolBatchLists);
    }

    public List<SchoolBatchListEntity> getSchoolBatchListByBatchId(Long batchId) {
        return schoolBatchListRepository.findByBatch_BatchId(batchId);
    }
}
