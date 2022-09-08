package com.example.labweb.service;

import com.example.labweb.domain.OTPData;
import com.example.labweb.repository.OTPDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OTPDataService {
    private OTPDataRepository otpDataRepository;

    @Autowired
    public OTPDataService(OTPDataRepository otpDataRepository){
        this.otpDataRepository = otpDataRepository;
    }

    public Optional<OTPData> getOTPData(){
        return otpDataRepository.findAll().stream().findAny();
    }

    public void deleteOTPData(){
        otpDataRepository.deleteAll();
    }

    public OTPData changeOTPData(String key){
        deleteOTPData();
        OTPData data = new OTPData(key);
        otpDataRepository.save(data);
        return data;
    }
}
