package com.example.labweb.service;

import com.example.labweb.configuration.StorageProperties;
import com.example.labweb.domain.EmployInfo;
import com.example.labweb.repository.EmployInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class EmployInfoService {

    private EmployInfoRepository employInfoRepository;
    private StorageProperties storageProperties;

    @Autowired
    public EmployInfoService(EmployInfoRepository employInfoRepository,
                             StorageProperties storageProperties){
        this.employInfoRepository = employInfoRepository;
        this.storageProperties = storageProperties;
    }

    public List<EmployInfo> findAll(){
        return employInfoRepository.findAll();
    }

    public Resource loadAsResource(String filename){
        try{
            Path file = Paths.get(storageProperties.getProfile_location()).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable())
                return resource;
            else return null;
        } catch(MalformedURLException e){
            return null;
        }
    }
}
