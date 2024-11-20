package com.example.demo.model.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.repository.BlogRepository;
import com.example.demo.model.repository.BoardRepository;
import com.example.demo.model.repository.Member_Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Service
@Transactional // 트랜잭션처리(클래스내모든메소드대상)
@RequiredArgsConstructor
public class Member_Service {

    @Autowired
    private final Member_Repository member_Repository; // 리포지토리 선언

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 스프링버전5 이후, 단방향해싱알고리즘지원

    private void validateDuplicateMember(AddMemberRequest request){
    Member findMember= memberRepository.findByEmail(request.getEmail()); // 이메일존재유무
    if(findMember!= null){
    throw new IllegalStateException("이미가입된회원입니다."); // 예외처리
    }
    }
    public Member saveMember(AddMemberRequest request){
        validateDuplicateMember(request); // 이메일 체크

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword); // 암호화된 비밀번호 설정
        return memberRepository.save(request.toEntity());
    }
}


    

    

    


    
    
}
