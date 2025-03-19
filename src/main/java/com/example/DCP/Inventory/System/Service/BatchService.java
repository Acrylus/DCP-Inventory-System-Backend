package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.BatchEntity;
import com.example.DCP.Inventory.System.Entity.SchoolBatchListEntity;
import com.example.DCP.Inventory.System.Repository.BatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public List<BatchEntity> getAllBatches() {
        List<BatchEntity> batches = batchRepository.findAll();

        for (BatchEntity batch : batches) {
            int totalPackages = batch.getSchoolBatchList().stream()
                    .mapToInt(SchoolBatchListEntity::getNumberOfPackage)
                    .sum();

            batch.setNumberOfPackage(totalPackages);
        }

        return batchRepository.saveAll(batches);
    }

    public BatchEntity getBatchById(Long id) {
        BatchEntity batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        int totalPackages = batch.getSchoolBatchList().stream()
                .mapToInt(SchoolBatchListEntity::getNumberOfPackage)
                .sum();

        batch.setNumberOfPackage(totalPackages);

        return batchRepository.save(batch);
    }

    public BatchEntity updateBatch(Long id, BatchEntity batchDetails) {
        // Check if the batch exists
        BatchEntity batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        // Update the batch fields with the details from batchDetails
        batch.setBatchName(batchDetails.getBatchName());
        batch.setBudgetYear(batchDetails.getBudgetYear());
        batch.setDeliveryYear(batchDetails.getDeliveryYear());
        batch.setPrice(batchDetails.getPrice());
        batch.setSupplier(batchDetails.getSupplier());
        batch.setNumberOfPackage(batchDetails.getNumberOfPackage());
        batch.setRemarks(batchDetails.getRemarks());

        // Update associated entities
        batch.setSchoolBatchList(batchDetails.getSchoolBatchList());
        batch.setConfigurations(batchDetails.getConfigurations());

        // Save and return the updated batch
        return batchRepository.save(batch);
    }

    public BatchEntity saveBatch(BatchEntity batch) {
        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }

    public void saveAll(List<BatchEntity> batches) {
        batchRepository.saveAll(batches);
    }
}
