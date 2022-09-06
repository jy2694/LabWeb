package com.example.labweb.service;

import com.example.labweb.domain.Article;
import com.example.labweb.dto.ArticlePostRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
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
    public Optional<Member> updateById(String id, MemberSignupRequestDTO dto){
        Optional<Member> e = memberRepository.findById(id);
        e.ifPresent(member -> {
            if(dto.getStudentId() != null)
                member.setStudentId(dto.getStudentId());
            if(dto.getPhone() != null)
                member.setPhone(dto.getPhone());
            if(dto.getResearcherId() != null)
                member.setResearcherId(dto.getResearcherId());
            if(dto.getEmail() != null)
                member.setEmail(dto.getEmail());
            if(dto.getBirth() != null)
                member.setBirth(dto.getBirth());
            memberRepository.save(member);
        });
        return e;
    }


}
