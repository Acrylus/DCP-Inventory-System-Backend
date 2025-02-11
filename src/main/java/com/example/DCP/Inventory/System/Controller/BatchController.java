package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.BatchEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.BatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    // Get all batches
    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllBatches() {
        List<BatchEntity> batches = batchService.getAllBatches();
        return Response.response(HttpStatus.OK, "Batches found", batches);
    }

    // Get batch by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getBatchById(@PathVariable Long id) {
        try {
            BatchEntity batchEntity = batchService.getBatchById(id);
            return Response.response(HttpStatus.OK, "Batch found", batchEntity);
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Batch not found");
        }
    }

    // Create a new batch
    @PostMapping("/create")
    public ResponseEntity<Object> createBatch(@RequestBody BatchEntity batch) {
        BatchEntity createdBatch = batchService.saveBatch(batch);
        return Response.response(HttpStatus.CREATED, "Batch created successfully", createdBatch);
    }

    // Update batch by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateBatch(@PathVariable Long id, @RequestBody BatchEntity batchDetails) {
        try {
            BatchEntity updatedBatch = batchService.updateBatch(id, batchDetails);
            return Response.response(HttpStatus.OK, "Batch updated successfully", updatedBatch);
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Batch not found");
        }
    }

    // Delete batch by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteBatch(@PathVariable Long id) {
        try {
            batchService.deleteBatch(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "Batch deleted successfully");
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Batch not found");
        }
    }

    @PostMapping("/create_all")
    public ResponseEntity<Object> addBatches(@RequestBody List<BatchEntity> batches) {
        try {
            batchService.saveAll(batches);
            return ResponseEntity.status(HttpStatus.CREATED).body("Batches added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add Batches");
        }
    }
}
