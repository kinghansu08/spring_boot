package com.example.demo.model.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.repository.BlogRepository;
import com.example.demo.model.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class BlogService {

    @Autowired
    //private final BlogRepository blogRepository;
     private final BlogRepository blogRepository; // 리포지토리 선언


      // 게시판 전체 목록 조회
    //public List<Article> findAll() {
      //  return blogRepository.findAll();
    //}

    //게시판 특정 글 조회
    // public Optional<Article> findById(Long id) {
    //     return blogRepository.findById(id);
    // }

    public List<Board> findAll() { // 게시판 전체 목록 조회
    return blogRepository.findAll();
    }

    public Optional<Board> findById(Long id) { // 게시판 특정 글 조회
     return blogRepository.findById(id);
    }

    public Page<Board> findAll(Pageable pageable) {
      return blogRepository.findAll(pageable);
      }
      public Page<Board> searchByKeyword(String keyword, Pageable pageable) {
      return blogRepository.findByTitleContainingIgnoreCase(keyword, pageable);
      } // LIKE 검색 제공(대소문자 무시)

    public Board save(AddArticleRequest request){
      // DTO가 없는 경우 이곳에 직접 구현 가능
     return blogRepository.save(request.toEntity());
      }

     
    // 게시글 수정
    // public void update(Long id, AddArticleRequest request) {
    //     Optional<Article> optionalArticle = blogRepository.findById(id); // 단일글조회
    //    optionalArticle.ifPresent(article -> { //값이있으면
    //    article.update(request.getTitle(), request.getContent()); // 값을수정
    //    blogRepository.save(article); // Article 객체에저장
    //    });
    //     }


    // // 게시글 저장
    // public Article save(AddArticleRequest request) {
    //     return blogRepository.save(request.toEntity());
    // }
    // public void delete(Long id) {
    //     blogRepository.deleteById(id);
    //     }
}
