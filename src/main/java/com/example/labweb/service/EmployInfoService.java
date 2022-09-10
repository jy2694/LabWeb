package com.example.labweb.service;

import com.example.labweb.domain.EmployInfo;
import com.example.labweb.repository.EmployInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployInfoService {

    private EmployInfoRepository employInfoRepository;

    @Autowired
    public EmployInfoService(EmployInfoRepository employInfoRepository){
        this.employInfoRepository = employInfoRepository;
    }

    public List<EmployInfo> findAll(){
        return employInfoRepository.findAll();
    }
}
