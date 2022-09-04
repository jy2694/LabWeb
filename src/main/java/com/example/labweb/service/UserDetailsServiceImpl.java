package com.example.labweb.service;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.domain.MemberInterface;
import com.example.labweb.domain.ProfMember;
import com.example.labweb.repository.GraduateMemberRepository;
import com.example.labweb.repository.MemberRepository;
import com.example.labweb.repository.ProfMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final MemberRepository memberRepository;
    private final GraduateMemberRepository graduateMemberRepository;
    private final ProfMemberRepository profMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findById(username);
        if(member.isPresent())
            return new UserDetailsImpl(member.get());
        Optional<GraduateMember> gmember = graduateMemberRepository.findById(username);
        if(gmember.isPresent())
            return new UserDetailsImpl(gmember.get());
        Optional<ProfMember> pmember = profMemberRepository.findById(username);
        if(pmember.isPresent())
            return new UserDetailsImpl(pmember.get());
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }
}
