package com.example.labweb.repository;

import com.example.labweb.domain.ProjectSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectScheduleRepository extends JpaRepository<ProjectSchedule, Long> {
}
