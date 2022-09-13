package com.example.labweb.service;

import com.example.labweb.domain.ProjectSchedule;
import com.example.labweb.repository.ProjectScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ProjectScheduleService {
    private ProjectScheduleRepository projectScheduleRepository;

    public List<ProjectSchedule> findAll(){
        return projectScheduleRepository.findAll();
    }
}
