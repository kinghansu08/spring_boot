package com.example.demo.controller;

import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@Controller
@RequiredArgsConstructor
public class BlogController{

    private final BlogService blogService;

   /*  @GetMapping("/article_list")
    public String getArticles(Model model) {
        List<Article> articles = blogService.findAll();
        model.addAttribute("articles", articles);
        return "article_list";  // article_list.html 페이지로 이동
    }*/

    // POST 요청으로 게시글 추가
    /*@PostMapping("/api/articles")
    public String addArticle(@ModelAttribute AddArticleRequest request) {
        blogService.save(request);  // 게시글 저장
        return "redirect:/article_list";  // 게시글 추가 후 목록 페이지로 리다이렉트
    }*/

    /*@PutMapping("/api/article_edit/{id}")
    public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
        blogService.update(id, request);
        return "redirect:/article_list"; // 글 수정 후 목록 페이지로 리다이렉트
    }*/

    // @PutMapping("/api/board_edit/{id}")
    // public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    //     blogService.update(id, request);
    //     return "redirect:/board_list"; // 글 수정 후 목록 페이지로 리다이렉트
    // }

    /*@DeleteMapping("/api/article_delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";  // 삭제 후 목록 페이지로 리다이렉트
    }*/

    // @DeleteMapping("/api/board_delete/{id}")
    // public String deleteArticle(@PathVariable Long id) {
    //     blogService.delete(id);
    //     return "redirect:/board_list";  // 삭제 후 목록 페이지로 리다이렉트
    // }


    /*@GetMapping("/article_edit/{id}")
    public String articleEdit(Model model, @PathVariable Long id) {
        Optional<Article> article = blogService.findById(id);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
        } else {
            return "error_page/article_error"; // 오류 페이지로 리다이렉트
        }
        return "article_edit"; // article_edit.html 페이지 반환
    }*/


    @GetMapping("/board_list") // 새로운 게시판 링크 지정
public String board_list(
    Model model, 
    @RequestParam(defaultValue = "0") int page, 
    @RequestParam(defaultValue = "") String keyword, 
    HttpSession session) { // 세션 객체 전달

    // 세션에서 userId 확인
    String userId = (String) session.getAttribute("userId");
    String email = (String) session.getAttribute("email"); // 세션에서 이메일 확인
    if (userId == null) {
        // 세션이 없으면 로그인 페이지로 리다이렉션
        return "redirect:/member_login";
    }
    // 디버깅: 세션의 userId를 출력
    System.out.println("세션 userId: " + userId);

    // 페이지 요청 및 검색 처리
    PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
    Page<Board> list; // Page를 반환
    if (keyword.isEmpty()) {
        list = blogService.findAll(pageable); // 기본 전체 출력(키워드 없음)
    } else {
        list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
    }

    // 모델에 데이터 추가
    model.addAttribute("boards", list); // 게시판 목록
    model.addAttribute("totalPages", list.getTotalPages()); // 총 페이지 수
    model.addAttribute("currentPage", page); // 현재 페이지
    model.addAttribute("keyword", keyword); // 검색 키워드
    model.addAttribute("email", email); // 로그인 사용자(이메일)
    // model.addAttribute("startNum", startNum); // 10주차 ppt에는 적혀져 있는데, 일단 보류

    // 연결할 HTML 파일 이름 반환
    return "board_list";
}

    @GetMapping("/board_view/{id}") // 게시판 링크 지정
    public String board_view(Model model, @PathVariable Long id) {
     Optional<Board> list = blogService.findById(id); // 선택한 게시판 글

     if (list.isPresent()) {
        model.addAttribute("boards", list.get()); // 존재할 경우 실제 Article 객체를 모델에 추가
       } else {
        // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
        return "/error_page/article_error"; // 오류 처리 페이지로 연결
       }
        return "board_view"; // .HTML 연결
}
@GetMapping("/board_write") // Specify new bulletin board link
    public String board_write(Model model) {
        List<Board> list = blogService.findAll(); // Full list of bulletin boards
        model.addAttribute("boards", list); // add to model
        return "board_write"; // .HTML connection
    }
    @PostMapping("/api/boards") // 글쓰기 게시판 저장
    public String addboards(@ModelAttribute AddArticleRequest request) {
     blogService.save(request);
     return "redirect:/board_list"; // .HTML 연결
    }
}
