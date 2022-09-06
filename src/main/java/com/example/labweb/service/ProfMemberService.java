package com.example.labweb.service;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.ProfMember;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.repository.GraduateMemberRepository;
import com.example.labweb.repository.ProfMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfMemberService {
    @Autowired
    private ProfMemberRepository profMemberRepository;

    public List<ProfMember> findAll() {
        List<ProfMember> members = new ArrayList<>();
        profMemberRepository.findAll().forEach(e -> members.add(e));
        return members;
    }

    public Optional<ProfMember> findById(String id) {
        Optional<ProfMember> member = profMemberRepository.findById(id);
        return member;
    }

    public Optional<ProfMember> findByName(String name){
        Optional<ProfMember> member = profMemberRepository.findAll().stream()
                .filter(memberEntity -> memberEntity.getName().equals(name))
                .findAny();
        return member;
    }

    public void deleteById(String id) {
        profMemberRepository.deleteById(id);
    }

    public ProfMember save(ProfMember member) {
        profMemberRepository.save(member);
        return member;
    }

    public Optional<ProfMember> updateById(String id, MemberSignupRequestDTO dto){
        Optional<ProfMember> e = profMemberRepository.findById(id);
        e.ifPresent(member -> {
            if(dto.getPhone() != null)
                member.setPhone(dto.getPhone());
            if(dto.getEmail() != null)
                member.setEmail(dto.getEmail());
            if(dto.getBirth() != null)
                member.setBirth(dto.getBirth());
            profMemberRepository.save(member);
        });
        return e;
    }
}
