package com.example.labweb.service;

import com.example.labweb.repository.MemberRepository;
import com.example.labweb.vo.MemberEntity;
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

    public Optional<MemberEntity> findById(Long mbrNo) {
        Optional<MemberEntity> member = memberRepository.findById(mbrNo);
        return member;
    }

    public void deleteById(Long mbrNo) {
        memberRepository.deleteById(mbrNo);
    }

    public MemberEntity save(MemberEntity member) {
        memberRepository.save(member);
        return member;
    }

    public void updateById(Long mbrNo, MemberEntity member) {
        Optional<MemberEntity> e = memberRepository.findById(mbrNo);

        if (e.isPresent()) {
            e.get().setMbrNo(member.getMbrNo());
            e.get().setId(member.getId());
            e.get().setName(member.getName());
            memberRepository.save(member);
        }
    }
}
