package com.example.demo.controller;

import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


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
    public String board_list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
     PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
    Page<Board> list; // Page를 반환
    if (keyword.isEmpty()) {
     list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
     } else {
     list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
    }
     model.addAttribute("boards", list); // 모델에 추가
    model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
    model.addAttribute("currentPage", page); // 페이지 번호
    model.addAttribute("keyword", keyword); // 키워드
    return "board_list"; // .HTML 연결
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
