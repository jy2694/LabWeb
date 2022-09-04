package com.example.labweb.service;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.domain.MemberInterface;
import com.example.labweb.dto.JwtRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.repository.GraduateMemberRepository;
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
    private final GraduateMemberRepository graduateMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public MemberInterface signup(MemberSignupRequestDTO request){
        if(memberRepository.findById(request.getId()).isPresent()) return null;
        if(graduateMemberRepository.findById(request.getId()).isPresent()) return null;
        if(request.getResearcherId() == null){
            GraduateMember member = new GraduateMember(request);
            member.encryptPassword(passwordEncoder);
            graduateMemberRepository.save(member);
            return member;
        } else {
            Member member = new Member(request);
            member.encryptPassword(passwordEncoder);
            memberRepository.save(member);
            return member;
        }
    }

    public MemberInterface signin(JwtRequestDTO request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getID(), request.getPW()));
        System.out.println("SUCCESS");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Member> member = memberRepository.findById(principal.getUsername());
        Optional<GraduateMember> gmember = graduateMemberRepository.findById(principal.getUsername());
        if(member.isPresent())
            return member.get();
        else if(gmember.isPresent())
            return gmember.get();
        else return null;
    }
}
