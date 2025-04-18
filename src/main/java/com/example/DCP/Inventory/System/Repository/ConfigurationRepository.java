package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {
    List<ConfigurationEntity> findByBatch_BatchId(Long batchId);

    Optional<ConfigurationEntity> findById_BatchIdAndId_ConfigurationId(Long batchId, Long configurationId);

    @Query("SELECT MAX(c.id.configurationId) FROM ConfigurationEntity c WHERE c.id.batchId = :batchId")
    Long findMaxConfigIdForBatch(@Param("batchId") Long batchId);

    @Modifying
    @Query("DELETE FROM ConfigurationEntity c WHERE c.id.batchId = :batchId")
    void deleteByBatch(@Param("batchId") Long batchId);

    List<ConfigurationEntity> findById_BatchIdOrderById_ConfigurationId(Long batchId);
}
