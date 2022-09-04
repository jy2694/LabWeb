package com.example.labweb.service;

import com.example.labweb.repository.GraduateMemberRepository;
import com.example.labweb.repository.MemberRepository;
import com.example.labweb.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        memberRepository.findAll().forEach(e -> members.add(e));
        return members;
    }

    public Optional<Member> findById(String id) {
        Optional<Member> member = memberRepository.findById(id);
        return member;
    }

    public Optional<Member> findByName(String name){
        Optional<Member> member = memberRepository.findAll().stream()
                .filter(memberEntity -> memberEntity.getName().equals(name))
                .findAny();
        return member;
    }

    public void deleteById(String id) {
        memberRepository.deleteById(id);
    }

    public Member save(Member member) {
        memberRepository.save(member);
        return member;
    }

    public void updateById(String id, Member member) {
        Optional<Member> e = memberRepository.findById(id);

        if (e.isPresent()) {
            e.get().setId(member.getId());
            e.get().setName(member.getName());
            e.get().setPassword(member.getPassword());
            e.get().setResearcherId(member.getResearcherId());
            e.get().setStudentId(member.getStudentId());
            e.get().setEmail(member.getEmail());
            memberRepository.save(member);
        }
    }


}
