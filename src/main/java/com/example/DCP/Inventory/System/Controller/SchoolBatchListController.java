package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.SchoolBatchListEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.SchoolBatchListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school_batch_list")
public class SchoolBatchListController {

    private final SchoolBatchListService schoolBatchListService;

    public SchoolBatchListController(SchoolBatchListService schoolBatchListService) {
        this.schoolBatchListService = schoolBatchListService;
    }

    @GetMapping("/get_all")
    public List<SchoolBatchListEntity> getAllSchoolBatchLists() {
        return schoolBatchListService.getAllSchoolBatchLists();
    }

    @GetMapping("/get/{id}")
    public SchoolBatchListEntity getSchoolBatchListById(@PathVariable Long id) {
        return schoolBatchListService.getSchoolBatchListById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateSchoolBatchList(@PathVariable Long id, @RequestBody SchoolBatchListEntity updatedBatchList) {
        try {
            SchoolBatchListEntity updatedEntity = schoolBatchListService.updateSchoolBatchList(id, updatedBatchList);
            return Response.response(HttpStatus.OK, "School batch list updated successfully", updatedEntity);
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, e.getMessage()); // 404 if not found
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating school batch list");
        }
    }

    @PostMapping("create")
    public ResponseEntity<Object> createSchoolBatchList(@RequestBody SchoolBatchListEntity batchSchoolList) {
        try {
            schoolBatchListService.saveSchoolBatchList(batchSchoolList);
            return ResponseEntity.status(HttpStatus.CREATED).body("School Batch List added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add School Batch List");
        }
    }
    
    @PostMapping("/create_all")
    public ResponseEntity<Object> addSchoolBatchLists(@RequestBody List<SchoolBatchListEntity> schoolBatchList) {
        try {
        	schoolBatchListService.saveAll(schoolBatchList);
            return ResponseEntity.status(HttpStatus.CREATED).body("School Batch Lists added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add School Batch Lists");
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSchoolBatchList(@PathVariable Long id) {
        schoolBatchListService.deleteSchoolBatchList(id);
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<SchoolBatchListEntity>> getSchoolBatchListByBatch(@PathVariable Long batchId) {
        List<SchoolBatchListEntity> schoolBatchLists = schoolBatchListService.getSchoolBatchListByBatchId(batchId);
        return ResponseEntity.ok(schoolBatchLists);
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<List<SchoolBatchListEntity>> getSchoolBatchListBySchoolRecord(@PathVariable Long schoolId) {
        List<SchoolBatchListEntity> schoolBatchListsSchool = schoolBatchListService.getSchoolBatchListBySchoolRecordId(schoolId);
        return ResponseEntity.ok(schoolBatchListsSchool);
    }
}
