package com.example.demo.model.domain;

import lombok.*; // 어노테이션 자동 생성
import jakarta.persistence.*; // 기존 javax 후속 버전

@Getter
@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title = "";

    @Column(name = "content", nullable = false)
    private String content = "";

    @Column(name = "user", nullable = false)
    private String user = "";

    @Column(name = "newdate", nullable = false)
    private String newdate = "";

    @Column(name = "count", nullable = false)
    private String count = "";

    @Column(name = "likec", nullable = false)
    private String likec = "";

    //1204추가
    @Column(name = "update", nullable = false)
    private String update = "";

    @Builder
    public Board(String title, String content, String user, String newdate, String count, String likec, String update) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
        this.update = update;
    }

    public void update(String title, String content,String user,String newdate,String count,String likec,String update) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
        this.update = update;
    }
}

