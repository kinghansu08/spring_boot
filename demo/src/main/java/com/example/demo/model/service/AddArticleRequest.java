package com.example.demo.model.service;

import lombok.*; // 어노테이션 자동 생성

import com.example.demo.model.domain.Board;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data // getter, setter, toString, equals 등 자동 생성
public class AddArticleRequest {
    private String title;
    private String content;
    private String user;
    private String newdate;
    private String count;
    private String likec;

    // DTO를 Article 엔티티로 변환하는 메서드
    // public Article toEntity() { 
    //     return Article.builder()
    //         .title(title)
    //         .content(content)
    //         .build();
    // }
    // DTO를 board 엔티티로 변환하는 메서드

    public Board toEntity() { 
             return Board.builder()
                 .title(title)
                 .content(content)
                 .user(user)
                 .newdate(newdate)
                 .count(count)
                 .likec(likec)
                 .build();
         }

         // 추가된 getter 메서드 (Lombok이 자동으로 생성하지만 명시적으로 구현 가능)
    public String getUser() {
        return user;
    }

    public String getNewdate() {
        return newdate;
    }

    public String getCount() {
        return count;
    }

    public String getLikec() {
        return likec;
    }   
}
