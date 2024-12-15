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

    @GetMapping("/board_list")
public String board_list(
    Model model, 
    @RequestParam(defaultValue = "0") int page, 
    @RequestParam(defaultValue = "") String keyword, 
    HttpSession session) {

    // 세션에서 userId 확인
    String userId = (String) session.getAttribute("userId");
    String email = (String) session.getAttribute("email");
    if (userId == null) {
        return "redirect:/member_login"; // 로그인 페이지로 리다이렉션
    }

    // 페이지 처리 및 검색
    int pageSize = 3; // 페이지당 게시글 수
    PageRequest pageable = PageRequest.of(page, pageSize); 
    Page<Board> list = keyword.isEmpty() ? blogService.findAll(pageable)
                                         : blogService.searchByKeyword(keyword, pageable);

    // 게시글 번호 시작값 계산
    int startNum = (page * pageSize) + 1;

    // 데이터 추가
    model.addAttribute("boards", list.getContent()); // 게시글 리스트
    model.addAttribute("totalPages", list.getTotalPages()); // 총 페이지 수
    model.addAttribute("currentPage", page); // 현재 페이지
    model.addAttribute("keyword", keyword); // 검색어
    model.addAttribute("email", email); // 사용자 이메일
    model.addAttribute("startNum", startNum); // 게시글 시작 번호

    return "board_list"; // board_list.html 렌더링
}

@PostMapping("/api/board_update/{id}")
public String updateBoard(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    System.out.println("수정 요청 ID: " + id);
    System.out.println("수정 요청 데이터: " + request);
    blogService.update(id, request);
    return "redirect:/board_list";
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

    @DeleteMapping("/api/board_delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/board_list";
    }
    
    
}
