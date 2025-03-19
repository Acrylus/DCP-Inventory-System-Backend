package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.CoordinatorEntity;
import com.example.DCP.Inventory.System.Repository.CoordinatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordinatorService {

    private final CoordinatorRepository coordinatorRepository;

    public CoordinatorService(CoordinatorRepository coordinatorRepository) {
        this.coordinatorRepository = coordinatorRepository;
    }

    public List<CoordinatorEntity> getAllCoordinators() {
        return coordinatorRepository.findAll();
    }

    public CoordinatorEntity getCoordinatorById(Long id) {
        return coordinatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordinator not found"));
    }

    public CoordinatorEntity saveCoordinator(CoordinatorEntity coordinatorEntity) {
        return coordinatorRepository.save(coordinatorEntity);
    }

    public CoordinatorEntity updateCoordinator(Long id, CoordinatorEntity coordinatorDetails) {
        CoordinatorEntity coordinatorEntity = getCoordinatorById(id);
        coordinatorEntity.setName(coordinatorDetails.getName());
        coordinatorEntity.setEmail(coordinatorDetails.getEmail());
        coordinatorEntity.setNumber(coordinatorDetails.getNumber());
        coordinatorEntity.setSchool(coordinatorDetails.getSchool());
        coordinatorEntity.setDesignation(coordinatorDetails.getDesignation());
        coordinatorEntity.setRemarks(coordinatorDetails.getRemarks());

        return coordinatorRepository.save(coordinatorEntity);
    }

    public void deleteCoordinator(Long id) {
        coordinatorRepository.deleteById(id);
    }

    public List<CoordinatorEntity> getCoordinatorBySchoolRecordId(Long schoolRecordId) {
        return coordinatorRepository.findBySchool_SchoolRecordId(schoolRecordId);
    }
}
