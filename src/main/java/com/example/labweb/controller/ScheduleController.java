package com.example.labweb.controller;

import com.example.labweb.domain.LabSchedule;
import com.example.labweb.dto.CreateScheduleDTO;
import com.example.labweb.service.LabScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class ScheduleController {

    private LabScheduleService labScheduleService;

    public ScheduleController(LabScheduleService labScheduleService){
        this.labScheduleService = labScheduleService;
    }

    @PostMapping("/schedule")
    @ResponseBody
    public LabSchedule writeSchedule(CreateScheduleDTO dto){
        System.out.println(dto.getId());
        if(dto.getId() == null || dto.getId() == 0)
            return labScheduleService.save(dto);
        else {
            Optional<LabSchedule> schedule = labScheduleService.updateById(dto.getId(), dto);
            if(schedule.isPresent())
                return schedule.get();
            else
                return labScheduleService.save(dto);
        }
    }

    @PostMapping("/schedule/delete")
    @ResponseBody
    public void deleteSchedule(Long id){
        labScheduleService.deleteSchedule(id);
    }
}
