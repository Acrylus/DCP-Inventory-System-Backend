package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import com.example.DCP.Inventory.System.Entity.SchoolNTCEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolNTCRepository extends JpaRepository<SchoolNTCEntity, Long> {
    Optional<SchoolNTCEntity> findBySchool(SchoolEntity school);
    SchoolNTCEntity findBySchool_SchoolRecordId(Long schoolRecordId);
    @Modifying
    @Transactional
    @Query("DELETE FROM SchoolNTCEntity n WHERE n.school.schoolRecordId = :schoolId")
    void deleteBySchoolId(@Param("schoolId") Long schoolId);

}
