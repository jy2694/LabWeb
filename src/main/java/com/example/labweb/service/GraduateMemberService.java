package com.example.labweb.service;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.repository.GraduateMemberRepository;
import com.example.labweb.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GraduateMemberService {
    @Autowired
    private GraduateMemberRepository graduateMemberRepository;

    public List<GraduateMember> findAll() {
        List<GraduateMember> members = new ArrayList<>();
        graduateMemberRepository.findAll().forEach(e -> members.add(e));
        return members;
    }

    public Optional<GraduateMember> findById(String id) {
        Optional<GraduateMember> member = graduateMemberRepository.findById(id);
        return member;
    }

    public Optional<GraduateMember> findByName(String name){
        Optional<GraduateMember> member = graduateMemberRepository.findAll().stream()
                .filter(memberEntity -> memberEntity.getName().equals(name))
                .findAny();
        return member;
    }

    public Optional<GraduateMember> findByStudentId(String studentId){
        Optional<GraduateMember> member = graduateMemberRepository.findAll().stream()
                .filter(memberEntity -> memberEntity.getStudentId().equals(studentId))
                .findAny();
        return member;
    }

    public void deleteById(String id) {
        graduateMemberRepository.deleteById(id);
    }

    public GraduateMember save(GraduateMember member) {
        graduateMemberRepository.save(member);
        return member;
    }

    public Optional<GraduateMember> updateById(String id, MemberSignupRequestDTO dto){
        Optional<GraduateMember> e = graduateMemberRepository.findById(id);
        e.ifPresent(member -> {
            if(dto.getStudentId() != null)
                member.setStudentId(dto.getStudentId());
            if(dto.getPhone() != null)
                member.setPhone(dto.getPhone());
            if(dto.getEmail() != null)
                member.setEmail(dto.getEmail());
            if(dto.getBirth() != null)
                member.setBirth(dto.getBirth());
            graduateMemberRepository.save(member);
        });
        return e;
    }
}
