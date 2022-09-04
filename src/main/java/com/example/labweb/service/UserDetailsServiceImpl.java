package com.example.labweb.service;

import com.example.labweb.domain.GraduateMember;
import com.example.labweb.domain.Member;
import com.example.labweb.domain.MemberInterface;
import com.example.labweb.repository.GraduateMemberRepository;
import com.example.labweb.repository.MemberRepository;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findById(username);
        Optional<GraduateMember> gmember = graduateMemberRepository.findById(username);
        if(member.isPresent())
            return new UserDetailsImpl(member.get());
        else if(gmember.isPresent())
            return new UserDetailsImpl(gmember.get());
        else throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }
}
