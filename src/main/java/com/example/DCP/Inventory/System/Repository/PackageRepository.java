package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Entity.PackageEntity;
import com.example.DCP.Inventory.System.Entity.SchoolBatchListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    List<PackageEntity> findBySchoolBatchList_SchoolBatchId(Long schoolBatchId);
    Optional<PackageEntity> findById_SchoolBatchListIdAndId_PackageId(Long schoolBatchListId, Long packageId);
    void deleteBySchoolBatchList(SchoolBatchListEntity schoolBatchId);
    @Query("SELECT MAX(p.id.packageId) FROM PackageEntity p WHERE p.schoolBatchList.schoolBatchId = :schoolBatchId")
    Long findMaxPackageIdBySchoolBatchList(@Param("schoolBatchId") Long schoolBatchId);
    List<PackageEntity> findByConfiguration_Id_ConfigurationIdAndSchoolBatchList_Batch_BatchId(Long configurationId, Long batchId);
    List<PackageEntity> findBySchoolBatchList_Batch_BatchId(Long batchId);
    int countBySchoolBatchList(SchoolBatchListEntity schoolBatchList);
}
