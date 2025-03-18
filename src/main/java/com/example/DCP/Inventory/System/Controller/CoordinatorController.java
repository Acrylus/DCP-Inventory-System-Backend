package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Entity.CoordinatorEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.CoordinatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    // Get all coordinators
    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllCoordinators() {
        List<CoordinatorEntity> coordinators = coordinatorService.getAllCoordinators();
        return Response.response(HttpStatus.OK, "Coordinators found", coordinators);
    }

    // Get coordinator by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getCoordinatorById(@PathVariable Long id) {
        CoordinatorEntity coordinator = coordinatorService.getCoordinatorById(id);
        if (coordinator != null) {
            return Response.response(HttpStatus.OK, "Coordinator found", coordinator);
        } else {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Coordinator not found");
        }
    }

    // Create a new coordinator
    @PostMapping("/create")
    public ResponseEntity<Object> createCoordinator(@RequestBody CoordinatorEntity coordinatorEntity) {
        CoordinatorEntity createdCoordinator = coordinatorService.saveCoordinator(coordinatorEntity);
        return Response.response(HttpStatus.CREATED, "Coordinator created successfully", createdCoordinator);
    }

    // Update coordinator by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateCoordinator(
            @PathVariable Long id,
            @RequestBody CoordinatorEntity coordinatorDetails) {
        try {
            CoordinatorEntity updatedCoordinator = coordinatorService.updateCoordinator(id, coordinatorDetails);
            return Response.response(HttpStatus.OK, "Coordinator updated successfully", updatedCoordinator);
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Coordinator not found");
        }
    }

    // Delete coordinator by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCoordinator(@PathVariable Long id) {
        try {
            coordinatorService.deleteCoordinator(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "Coordinator deleted successfully");
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Coordinator not found");
        }
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<List<CoordinatorEntity>> getCoordinatorBySchool(@PathVariable Long schoolId) {
        List<CoordinatorEntity> coordinator = coordinatorService.getCoordinatorBySchoolId(schoolId);
        return ResponseEntity.ok(coordinator);
    }
}
