package com.example.labweb.controller;

import com.example.labweb.service.MemberService;
import com.example.labweb.domain.MemberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * JPA 참고용 클래스입니다. 필요가 없어지면 삭제하겠습니다
 *          - 제연
 */
@RestController
@RequestMapping("memberTest")
public class TestJpaRestController {
    // 기본형
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MemberService memberService;

    @Autowired
    public TestJpaRestController(MemberService memberService){
        this.memberService = memberService;
    }

    // 모든 회원 조회
    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<MemberEntity>> getAllmembers() {
        List<MemberEntity> member = memberService.findAll();
        return new ResponseEntity<List<MemberEntity>>(member, HttpStatus.OK);
    }

    // 회원번호로 한명의 회원 조회
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<MemberEntity> getMember(@PathVariable("id") String id) {
        Optional<MemberEntity> member = memberService.findById(id);
        return new ResponseEntity<MemberEntity>(member.orElseThrow(() -> new NoSuchElementException("존재하지 않는 계정입니다.")), HttpStatus.OK);
    }
/*
    @GetMapping(value = "/{name}", produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MemberEntity> getMember(@PathVariable("name") String name){
        Optional<MemberEntity> member = memberService.findByName(name);
        return new ResponseEntity<MemberEntity>(member.get(), HttpStatus.OK);
    }*/

    // 회원번호로 회원 삭제
    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deleteMember(@PathVariable("id") String id) {
        memberService.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    // 회원번호로 회원 수정(mbrNo로 회원을 찾아 Member 객체의 id, name로 수정함)
    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<MemberEntity> updateMember(@PathVariable("id") String id, MemberEntity member) {
        memberService.updateById(id, member);
        return new ResponseEntity<MemberEntity>(member, HttpStatus.OK);
    }

    // 회원 입력
    @PostMapping
    public ResponseEntity<MemberEntity> save(MemberEntity member) {
        return new ResponseEntity<MemberEntity>(memberService.save(member), HttpStatus.OK);
    }

    // 회원 입력
    @RequestMapping(value="/saveMember", method = RequestMethod.GET)
    public ResponseEntity<MemberEntity> save(HttpServletRequest req, MemberEntity member){
        return new ResponseEntity<MemberEntity>(memberService.save(member), HttpStatus.OK);
    }

}
