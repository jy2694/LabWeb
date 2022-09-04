package com.example.labweb.repository;

import com.example.labweb.domain.ProfMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfMemberRepository extends JpaRepository<ProfMember, String> {
}
