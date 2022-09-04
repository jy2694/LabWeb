package com.example.labweb.service;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
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

    public void deleteById(String id) {
        graduateMemberRepository.deleteById(id);
    }

    public GraduateMember save(GraduateMember member) {
        graduateMemberRepository.save(member);
        return member;
    }

    public void updateById(String id, GraduateMember member) {
        Optional<GraduateMember> e = graduateMemberRepository.findById(id);

        if (e.isPresent()) {
            e.get().setId(member.getId());
            e.get().setName(member.getName());
            e.get().setPassword(member.getPassword());
            e.get().setStudentId(member.getStudentId());
            e.get().setEmail(member.getEmail());
            graduateMemberRepository.save(member);
        }
    }
}
