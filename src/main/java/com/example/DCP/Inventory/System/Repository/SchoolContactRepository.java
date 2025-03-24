package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface SchoolContactRepository extends JpaRepository<SchoolContactEntity, Long> {
    Optional<SchoolContactEntity> findBySchool(SchoolEntity school);

    SchoolContactEntity findBySchool_SchoolRecordId(Long schoolRecordId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SchoolContactEntity c WHERE c.school.schoolRecordId = :schoolId")
    void deleteBySchoolId(@Param("schoolId") Long schoolId);

    @Query("SELECT sc FROM SchoolContactEntity sc JOIN FETCH sc.school")
    List<SchoolContactEntity> getAllWithSchool();

}
