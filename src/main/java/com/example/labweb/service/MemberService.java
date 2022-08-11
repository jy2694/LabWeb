package com.example.labweb.service;

import com.example.labweb.repository.MemberRepository;
import com.example.labweb.domain.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public List<MemberEntity> findAll() {
        List<MemberEntity> members = new ArrayList<>();
        memberRepository.findAll().forEach(e -> members.add(e));
        return members;
    }

    public Optional<MemberEntity> findById(String id) {
        Optional<MemberEntity> member = memberRepository.findById(id);
        return member;
    }

    public Optional<MemberEntity> findByName(String name){
        Optional<MemberEntity> member = memberRepository.findAll().stream()
                .filter(memberEntity -> memberEntity.getName().equals(name))
                .findAny();
        return member;
    }

    public void deleteById(String id) {
        memberRepository.deleteById(id);
    }

    public MemberEntity save(MemberEntity member) {
        memberRepository.save(member);
        return member;
    }

    public void updateById(String id, MemberEntity member) {
        Optional<MemberEntity> e = memberRepository.findById(id);

        if (e.isPresent()) {
            e.get().setId(member.getId());
            e.get().setName(member.getName());
            e.get().setPassword(member.getPassword());
            e.get().setRs_number(member.getRs_number());
            e.get().setSt_number(member.getSt_number());
            e.get().setEmail(member.getEmail());
            memberRepository.save(member);
        }
    }
}
