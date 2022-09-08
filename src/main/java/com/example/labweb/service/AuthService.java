package com.example.labweb.service;

import com.example.labweb.api.GoogleOtpAPI;
import com.example.labweb.domain.*;
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
    private final MemberService memberService;
    private final GraduateMemberService graduateMemberService;
    private final ProfMemberService profMemberService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final OTPDataService otpDataService;

    public int signup(MemberSignupRequestDTO request){
        if(memberService.findById(request.getId()).isPresent()
            || memberService.findByStudentId(request.getStudentId()).isPresent()) return 1;
        if(graduateMemberService.findById(request.getId()).isPresent()
            || graduateMemberService.findByStudentId(request.getStudentId()).isPresent()) return 1;
        if(profMemberService.findById(request.getId()).isPresent()) return 1;
        Optional<OTPData> otp = otpDataService.getOTPData();
        if(otp.isPresent() && !GoogleOtpAPI.checkCode(request.getOtp(), otp.get().getOtpkey())) return 2;
        if(request.getResearcherId() == null){
            if(request.getStudentId() == null){
                ProfMember member = new ProfMember(request);
                member.encryptPassword(passwordEncoder);
                profMemberService.save(member);
            } else {
                GraduateMember member = new GraduateMember(request);
                member.encryptPassword(passwordEncoder);
                graduateMemberService.save(member);
            }
        } else {
            Member member = new Member(request);
            member.encryptPassword(passwordEncoder);
            memberService.save(member);
        }
        return 0;
    }

    public MemberInterface signin(JwtRequestDTO request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getID(), request.getPW()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Member> member = memberService.findById(principal.getUsername());
        if(member.isPresent())
            return member.get();
        Optional<GraduateMember> gmember = graduateMemberService.findById(principal.getUsername());
        if(gmember.isPresent())
            return gmember.get();
        Optional<ProfMember> pmember = profMemberService.findById(principal.getUsername());
        if(pmember.isPresent())
            return pmember.get();
        return null;
    }
}
