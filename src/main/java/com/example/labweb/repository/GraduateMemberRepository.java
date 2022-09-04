package com.example.labweb.repository;

import com.example.labweb.domain.GraduateMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraduateMemberRepository  extends JpaRepository<GraduateMember, String> {
}
