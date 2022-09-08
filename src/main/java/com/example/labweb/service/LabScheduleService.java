package com.example.labweb.service;

import com.example.labweb.domain.LabSchedule;
import com.example.labweb.domain.ProfMember;
import com.example.labweb.dto.CreateScheduleDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.repository.LabScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LabScheduleService {
    private LabScheduleRepository labScheduleRepository;
    @Autowired
    public LabScheduleService(LabScheduleRepository labScheduleRepository){
        this.labScheduleRepository = labScheduleRepository;
    }

    public List<LabSchedule> findAll(){
        List<LabSchedule> elements = new ArrayList<>();
        labScheduleRepository.findAll().stream().forEach(schedule -> elements.add(schedule));
        return elements;
    }

    public LabSchedule save(CreateScheduleDTO dto){
        LabSchedule schedule = new LabSchedule(dto);
        labScheduleRepository.save(schedule);
        return schedule;
    }

    public Optional<LabSchedule> updateById(Long id, CreateScheduleDTO dto){
        Optional<LabSchedule> e = labScheduleRepository.findById(id);
        e.ifPresent(schedule -> {
            if(dto.getDescription() != null)
                schedule.setDescription(dto.getDescription());
            if(dto.getTitle() != null)
                schedule.setTitle(dto.getTitle());
            if(dto.getStart() != null)
                schedule.setStart(dto.getStart());
            if(dto.getEnd() != null)
                schedule.setEnd(dto.getEnd());
            if(dto.getAllDay() != null)
                schedule.setAllDay(dto.getAllDay());
            labScheduleRepository.save(schedule);
        });
        return e;
    }

    public void deleteSchedule(Long id){
        labScheduleRepository.deleteById(id);
    }
}
