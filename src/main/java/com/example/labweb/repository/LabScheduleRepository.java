package com.example.labweb.repository;

import com.example.labweb.domain.LabSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabScheduleRepository extends JpaRepository<LabSchedule, Long> {
}
