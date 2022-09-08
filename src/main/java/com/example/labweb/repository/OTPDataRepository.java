package com.example.labweb.repository;

import com.example.labweb.domain.OTPData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPDataRepository  extends JpaRepository<OTPData, Long> {

}
