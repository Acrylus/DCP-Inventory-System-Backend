package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.BatchEntity;
import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Entity.SchoolBatchListEntity;
import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolBatchListRepository extends JpaRepository<SchoolBatchListEntity, Long> {
    List<SchoolBatchListEntity> findByBatch_BatchId(Long batchId);
    List<SchoolBatchListEntity> findBySchool_SchoolId(Long schoolId);
}
