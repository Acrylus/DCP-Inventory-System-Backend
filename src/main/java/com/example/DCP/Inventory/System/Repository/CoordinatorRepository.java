package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.CoordinatorEntity;
import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinatorRepository extends JpaRepository<CoordinatorEntity, Long> {
    List<CoordinatorEntity> findBySchoolContact_School_SchoolRecordId(Long schoolRecordId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CoordinatorEntity c WHERE c.schoolContact = :schoolContact")
    void deleteBySchoolContact(@Param("schoolContact") SchoolContactEntity schoolContact);

    @Query("SELECT MAX(c.id.coordinatorId) FROM CoordinatorEntity c WHERE c.id.schoolContactId = :schoolContactId")
    Long findMaxCoordinatorId(@Param("schoolContactId") Long schoolContactId);
}
