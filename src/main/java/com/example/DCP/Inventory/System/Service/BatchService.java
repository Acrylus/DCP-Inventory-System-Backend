package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.BatchEntity;
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
        return batchRepository.findAll();
    }

    public BatchEntity getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
    }

    public BatchEntity updateBatch(Long id, BatchEntity batchDetails) {
        // Check if the batch exists
        BatchEntity batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        // Update the batch fields with the details from batchDetails
        batch.setBatchNumber(batchDetails.getBatchNumber());
        batch.setBudgetYear(batchDetails.getBudgetYear());
        batch.setDeliveryYear(batchDetails.getDeliveryYear());
        batch.setPrice(batchDetails.getPrice());
        batch.setSupplier(batchDetails.getSupplier());
        batch.setNumberOfPackage(batchDetails.getNumberOfPackage());
        batch.setRemarks(batchDetails.getRemarks());

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
