package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.Member_Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 1127추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.UUID;
// 1201에 추가된 내용
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid; // Jakarta EE (최신 버전)




@Controller
public class MemberController {
    @Autowired
    private Member_Service memberService;

     @GetMapping("/join_new") // 회원 가입 페이지 연결
public String join_new() {
 return "join_new"; // .HTML 연결
}

@PostMapping("/api/members") // 회원 가입 저장
public String addmembers(
    @Valid @ModelAttribute AddMemberRequest request) {
    memberService.saveMember(request);
    return "join_end"; // .HTML 연결
}

@GetMapping("/member_login") // 로그인 페이지 연결
public String member_login() {
 return "login"; // .HTML 연결
}
    

@PostMapping("/api/login_check") // 아이디, 패스워드 로그인 체크
public String checkMembers(@ModelAttribute AddMemberRequest request, Model model, HttpServletRequest request2, HttpServletResponse response) {
    try {
        // 기존 세션 확인 및 초기화
        HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
        if (session != null) {
            session.invalidate(); // 기존 세션 무효화
            Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID 초기화
            cookie.setPath("/"); // 쿠키 경로 설정
            cookie.setMaxAge(0); // 쿠키 삭제 (0으로 설정)
            response.addCookie(cookie); // 삭제된 쿠키를 응답으로 전달
        }

        // 새로운 세션 생성
        session = request2.getSession(true);


        // 로그인 체크: 이메일과 패스워드 검증
        Member member = memberService.loginCheck(request.getEmail(), request.getPassword());
        
        // 세션에 사용자 ID 저장
        String sessionId = UUID.randomUUID().toString(); // 고유 세션 ID 생성
        session.setAttribute("userId", sessionId); // 세션에 userId 저장
        
        // 로그인 성공 시 회원 정보를 모델에 추가
        model.addAttribute("member", member);
        
        String email = request.getEmail(); // 이메일 얻기
        session.setAttribute("userId", sessionId); // 아이디 이름 설정
        session.setAttribute("email", email); // 이메일 설정
        // 디버깅용 출력 (서버 로그에 표시)
        System.out.println("로그인 성공! 세션 userId: " + sessionId);
        
        
        // 로그인 성공 후 게시판 페이지로 이동
        return "redirect:/board_list";
    } catch (IllegalArgumentException e) {
        // 로그인 실패 시 에러 메시지를 모델에 추가
        model.addAttribute("error", e.getMessage());


        
        // 로그인 페이지로 리다이렉트
        return "login";
    }
}

@GetMapping("/api/logout") // 로그아웃버튼동작
public String member_logout(Model model, HttpServletRequest request2, HttpServletResponse response) {

    try {
    HttpSession session = request2.getSession(false); // 기존세션가져오기(존재하지않으면null 반환)
    session.invalidate(); // 기존세션무효화
    Cookie cookie= new Cookie("JSESSIONID", null); // JSESSIONID is the default session cookie name
    cookie.setPath("/"); // Set the path for the cookie
    cookie.setMaxAge(0); // Set cookie expiration to 0 (removes the cookie)
    response.addCookie(cookie); // Add cookie to the response
    session = request2.getSession(true); // 새로운세션생성
    System.out.println("세션userId: " + session.getAttribute("userId" )); // 초기화후IDE 터미널에세션값출력
    return "login"; // 로그인페이지로리다이렉트
} catch (IllegalArgumentException e) {
    model.addAttribute("error", e.getMessage()); // 에러메시지전달
    return "login"; // 로그인실패시로그인페이지로리다이렉트
}
}

}
