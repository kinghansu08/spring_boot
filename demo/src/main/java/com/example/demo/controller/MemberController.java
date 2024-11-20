package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public class MemberController {

     @GetMapping("/join_new") // 회원 가입 페이지 연결
public String join_new() {
 return "join_new"; // .HTML 연결
}

@PostMapping("/api/members") // 회원 가입 저장
public String addmembers(@ModelAttribute AddMemberRequest request) {
 memberService.saveMember(request);
 return "join_end"; // .HTML 연결
}
    
}
