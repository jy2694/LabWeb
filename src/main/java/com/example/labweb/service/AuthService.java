package com.example.labweb.service;

import com.example.labweb.domain.Member;
import com.example.labweb.dto.JwtRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Member signup(MemberSignupRequestDTO request){
        boolean exist = memberRepository.findAll().stream()
                .filter(memberEntity -> memberEntity.getId().equals(request.getId()))
                .count() >= 1;
        if(exist) return null;
        Member member = new Member(request);
        member.encryptPassword(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

    public Member signin(JwtRequestDTO request) throws Exception{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getID(), request.getPW()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Member> member = findById(principal.getUsername());
        if(member.isPresent())
            return member.get();
        else
            return null;
    }

    public Optional<Member> findById(String id){
        return memberRepository.findAll().stream().filter(member -> member.getId().equals(id)).findAny();
    }

    public List<Member> findByName(String name){
        return memberRepository.findAll().stream().filter(member -> member.getName().equals(name)).collect(Collectors.toList());
    }
}
