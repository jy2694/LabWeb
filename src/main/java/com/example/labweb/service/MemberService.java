package com.example.labweb.service;

import com.example.labweb.domain.Member;
import com.example.labweb.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {
    private MemberRepository memberRepository;

    public List<Member> findAll(){
        return memberRepository.findAll();
    }
}
