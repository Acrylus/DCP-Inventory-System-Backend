package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.ProviderEntity;
import com.example.DCP.Inventory.System.Entity.SchoolNTCEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    @Query("SELECT MAX(p.id.providerId) FROM ProviderEntity p WHERE p.id.schoolNTCId = :schoolNTCId")
    Long findMaxProviderId(@Param("schoolNTCId") Long schoolNTCId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProviderEntity c WHERE c.schoolNTC = :schoolNTC")
    void deleteBySchoolNTC(@Param("schoolNTC") SchoolNTCEntity schoolNtc);
}
