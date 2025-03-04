package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.DivisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends JpaRepository<DivisionEntity, Long> {
    boolean existsByDivision(String division);
}
