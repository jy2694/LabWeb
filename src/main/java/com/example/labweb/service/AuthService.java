package com.example.labweb.service;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.domain.MemberInterface;
import com.example.labweb.domain.ProfMember;
import com.example.labweb.dto.JwtRequestDTO;
import com.example.labweb.dto.MemberSignupRequestDTO;
import com.example.labweb.repository.GraduateMemberRepository;
import com.example.labweb.repository.MemberRepository;
import com.example.labweb.repository.ProfMemberRepository;
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
    private final ProfMemberRepository profMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public MemberInterface signup(MemberSignupRequestDTO request){
        if(memberRepository.findById(request.getId()).isPresent()) return null;
        if(graduateMemberRepository.findById(request.getId()).isPresent()) return null;
        if(profMemberRepository.findById(request.getId()).isPresent()) return null;
        if(request.getResearcherId() == null){
            if(request.getStudentId() == null){
                ProfMember member = new ProfMember(request);
                member.encryptPassword(passwordEncoder);
                profMemberRepository.save(member);
                return member;
            } else {
                GraduateMember member = new GraduateMember(request);
                member.encryptPassword(passwordEncoder);
                graduateMemberRepository.save(member);
                return member;
            }
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
        if(member.isPresent())
            return member.get();
        Optional<GraduateMember> gmember = graduateMemberRepository.findById(principal.getUsername());
        if(gmember.isPresent())
            return gmember.get();
        Optional<ProfMember> pmember = profMemberRepository.findById(principal.getUsername());
        return null;
    }
}
