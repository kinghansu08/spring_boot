# Proman-함께 일 할 동반자 모집!

## 개발 기간
- 9월 14일 (수) ~12월 15일 (일)
- 10월 31일 (수) 중간고사
- 12월 11일 (수) 기말고사
- (추가구현:ppt:7,9,10,11 구현 실패)

## 개발 환경
- Spring_Boot
- visual studio code

## 작성한 주요 코드 소개

### config

#### SecurityCofig.java
````package com.example.demo.config;

 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.security.config.annotation.web.builders.HttpSecurity;
 import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.security.web.SecurityFilterChain;

 @Configuration // 스프링 설정 클래스 지정, 등록된 Bean 생성 시점
 @EnableWebSecurity // 스프링 보안 활성화

 public class SecurityConfig { // 스프링에서 보안 관리 클래스

    @Bean // 명시적 의존성 주입 : Autowired와 다름
// 5.7버전 이전 WebSecurityConfigurerAdapter 사용
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
    .headers(headers -> headers
    .addHeaderWriter((request, response) -> {
        response.setHeader("X-XSS-Protection", "1; mode=block"); // XSS-Protection 헤더설정
    })
    )
    // .csrf(withDefaults())
    .csrf(csrf -> csrf.disable())
    .sessionManagement(session -> session
    .invalidSessionUrl("/session-expired") // 세션만료시이동페이지
    .maximumSessions(1) // 사용자별세션최대수
    .maxSessionsPreventsLogin(true) // 동시세션제한
    );
    // 설정을 비워둠
    return http.build(); // 필터 체인을 통해 보안설정(HttpSecurity)을 반환
}
@Bean // 암호화 설정
public PasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder(); // 비밀번호 암호화 저장
}    
}
````

### controller

#### BlogController.java
````package com.example.demo.controller;

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
````

#### FileController.java
````package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {
    @Value("${spring.servlet.multipart.location}") // properties 등록된 설정(경로) 주입
private String uploadFolder;
 @PostMapping("/upload-email")
 public String uploadEmail( // 이메일, 제목, 메시지를 전달받음
@RequestParam("email") String email,
 @RequestParam("subject") String subject,
 @RequestParam("message") String message,
 RedirectAttributes redirectAttributes) {
 try {
 Path uploadPath = Paths.get(uploadFolder).toAbsolutePath();
 if (!Files.exists(uploadPath)) {
 Files.createDirectories(uploadPath);
 }

 String sanitizedEmail= email.replaceAll("[^a-zA-Z0-9]", "_");
 Path filePath= uploadPath.resolve(sanitizedEmail+ ".txt"); // 업로드폴더에.txt이름설정
System.out.println("File path: " + filePath); // 디버깅용출력
try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
 writer.write("메일제목: "+subject); // 쓰기
writer.newLine(); // 줄바꿈
writer.write("요청메시지:");
 writer.newLine();
 writer.write(message);
 }
 redirectAttributes.addFlashAttribute("message", "메일내용이성공적으로업로드되었습니다!");

} catch (IOException e) {
    e.printStackTrace();
    redirectAttributes.addFlashAttribute("message", "업로드 중 오류가 발생했습니다.");
    return "/error_page/article_error"; // 오류 처리 페이지로 연결
   }
    return "upload_end"; // .html 파일 연동
   }
}
````
#### GlobalExceptionHandler.java
````package com.example.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // 모든 예외를 처리
    public String handleException(Exception ex, RedirectAttributes redirectAttributes, Model model) {
        // 예외 메시지를 모델에 추가하거나 리다이렉트 속성에 추가 가능
        model.addAttribute("errorMessage", ex.getMessage());
        return "error_page/article_error"; // 오류 처리 페이지로 이동
    }
}
````

#### MemberController.java
````package com.example.demo.controller;

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
````

### domain
#### Board.java
````package com.example.demo.model.domain;

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


    @Builder
    public Board(String title, String content, String user, String newdate, String count, String likec) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
    }

    public void update(String title, String content,String user,String newdate,String count,String likec) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
    }
 
}
````

#### Member.java
````package com.example.demo.model.domain;

import lombok.*; // 어노테이션 자동 생성
import jakarta.persistence.*; // 기존 javax 후속 버전

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 1씩 증가
    @Column(name = "id", updatable = false) // 수정 x
    private Long id;

    @Column(name = "name", nullable = false) // null x
    private String name = "";

    @Column(name = "email", unique = true, nullable = false) // unique 중복 x
    private String email = "";

    @Column(name = "password", nullable = false)
    private String password = "";

    @Column(name = "age", nullable = false)
    private String age = "";

    @Column(name = "mobile", nullable = false)
    private String mobile = "";

    @Column(name = "address", nullable = false)
    private String address = "";

    @Builder // 생성자에 빌더 패턴 적용(불변성)
    public Member(String name, String email, String password, String age, String mobile, String address){
    this.name = name;
    this.email = email;
    this.password = password;
    this.age = age;
    this.mobile = mobile;
    this.address = address;
    }
}
````

### repository
#### BlogRepository.java
````package com.example.demo.model.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.model.domain.Board;

@Repository
 public interface BlogRepository extends JpaRepository<Board, Long>{
 Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);
 }
````

#### Boardrepository.java
````package com.example.demo.model.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.domain.Board;

//@Repository
//public interface BlogRepository extends JpaRepository<Article, Long>{
//}
@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
    // List<Article> findAll();
}
````

#### Member_Service.java
````package com.example.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.domain.Member;

@Repository
 public interface Member_Repository extends JpaRepository<Member, Long> {
 Member findByEmail(String email);
 }
````

### service
#### AddArticleRequest.java
````package com.example.demo.model.service;

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
````
#### AddMemberRequest.java
````
package com.example.demo.model.service;

import com.example.demo.model.domain.Member;
import lombok.*; // 어노테이션 자동 생성\

import jakarta.validation.constraints.*;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data // getter, setter, toString, equals 등 자동 생성
public class AddMemberRequest {
    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    @Email(message = "유효하지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 입력하세요.")
    private String password;

    @NotNull(message = "나이는 필수 입력 사항입니다.")
    @Min(value = 1, message = "나이는 1세 이상이어야 합니다.")
    @Max(value = 100, message = "나이는 100세 이하로 입력하세요.")
    private Integer age;

    @NotBlank(message = "휴대폰 번호는 필수 입력 사항입니다.")
    private String mobile;

    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    private String address;
        
        public Member toEntity(){ // Member 생성자를 통해 객체 생성
            return Member.builder()
             .name(name)
             .email(email)
             .password(password)
             .age(String.valueOf(age))
             .mobile(mobile)
             .address(address)
             .build();
        }
    }
````
#### BlogService.java
````package com.example.demo.model.service;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.domain.Board;
import com.example.demo.model.repository.BlogRepository;

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
    public void update(Long id, AddArticleRequest request) {
      System.out.println("Service에서 수정 요청 ID: " + id);
      System.out.println("Service에서 수정 요청 데이터: " + request);
      Optional<Board> optionalBoard = blogRepository.findById(id);
      if (optionalBoard.isPresent()) {
          Board board = optionalBoard.get();
          board.update(request.getTitle(), request.getContent(), request.getUser(),
                       request.getNewdate(), request.getCount(), request.getLikec());
          blogRepository.save(board);
          System.out.println("수정 완료: " + board);
      } else {
          throw new IllegalArgumentException("해당 ID의 게시글이 없습니다: " + id);
      }
  }
  
      public void delete(Long id) {
        Optional<Board> optionalBoard = blogRepository.findById(id); // 삭제할 게시글 조회
        if (optionalBoard.isPresent()) {
            blogRepository.deleteById(id); // 게시글 삭제
        } else {
            throw new IllegalArgumentException("삭제할 게시글이 존재하지 않습니다. ID: " + id);
        }
    }

    // // 게시글 저장
    // public Article save(AddArticleRequest request) {
    //     return blogRepository.save(request.toEntity());
    // }
    // public void delete(Long id) {
    //     blogRepository.deleteById(id);
    //     }
}
````

#### Member_Service.java
````package com.example.demo.model.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.demo.model.repository.Member_Repository;

//1125이후 추가한 내용
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid; // Jakarta EE (최신 버전)
import org.springframework.validation.annotation.Validated;



@Service
@Validated
@Transactional // 트랜잭션처리(클래스내모든메소드대상)
@RequiredArgsConstructor
public class Member_Service {
    // @Autowired
    // private final Member_Repository member_Repository; // 리포지토리 선언

    private final Member_Repository memberRepository;
    private final PasswordEncoder passwordEncoder;  //스프링버전5 이후, 단방향해싱알고리즘지원

    private void validateDuplicateMember(AddMemberRequest request){
    Member findMember= memberRepository.findByEmail(request.getEmail()); // 이메일존재유무
    if(findMember!= null){
    throw new IllegalStateException("이미가입된회원입니다."); // 예외처리
    }
    }
    
    public Member saveMember(@Valid AddMemberRequest request){
        validateDuplicateMember(request); // 이메일 체크

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword); // 암호화된 비밀번호 설정
        return memberRepository.save(request.toEntity());
    }

    public Member loginCheck(String email, String rawPassword) {
    Member member = memberRepository.findByEmail(email); // 이메일 조회
    if (member == null) {
    throw new IllegalArgumentException("등록되지 않은 이메일입니다.");
    }
    if (!passwordEncoder.matches(rawPassword, member.getPassword())) { // 비밀번호 확인
    throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    return member; // 인증 성공 시 회원 객체 반환
    }

}
````
### Templates

#### error_page ->article_error.html
````
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
<h1 class="mt-5 text-danger">블로그게시판-잘못된접근</h1>
<!--오류메시지-->
<div class="alert alert-danger mt-4" role="alert">
<h4 class="alert-heading">잘못된게시판접근입니다!</h4>
<p>요청하신게시글을찾을수없거나, 접근권한이없습니다.</p>
<hr>
<p class="mb-0">이전페이지로돌아가시려면아래버튼을클릭해주세요.</p>
</div>
<!--이전페이지로돌아가는버튼-->
<div class="mt-4">
<a href="javascript:history.back()" class="btn btn-primary">이전페이지로</a>
</div>
</div>
</body>
</html>
````
#### board_list.html
````
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <!-- 헤더 -->
    <div class="d-flex justify-content-between align-items-center mt-4 p-3 border rounded bg-light">
        <h1 class="m-0">블로그 게시판</h1>
        <div class="text-right">
            <span class="font-weight-bold text-primary" th:text="${email} + '님 환영합니다.'"></span>
            <a class="btn btn-sm btn-outline-danger ml-3" th:href="@{/api/logout}">로그아웃</a>
        </div>
    </div>

    <!-- 검색 -->
    <form th:action="@{/board_list}" method="get" class="mt-4">
        <div class="input-group">
            <input type="text" name="keyword" th:value="${keyword}" class="form-control" placeholder="검색어 입력...">
            <button type="submit" class="btn btn-primary">검색</button>
        </div>
    </form>

    <!-- 게시글 목록 -->
    <div class="mt-5">
        <h2>게시글 목록</h2>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>글번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>조회수</th>
                    <th>좋아요</th>
                    <!-- <th>관리</th> -->
                </tr>
            </thead>
            <tbody>
                <!-- 게시글 목록 -->
                <tr th:each="board, iterStat : ${boards}">
                    <td th:text="${startNum != null ? startNum + iterStat.index : iterStat.index + 1}"></td>
                    <td>
                        <a th:href="@{/board_view/{id}(id=${board.id})}" th:text="${board.title}"></a>
                    </td>
                    <td th:text="${board.user}"></td>
                    <td th:text="${board.newdate}"></td>
                    <td th:text="${board.count}"></td>
                    <td th:text="${board.likec}"></td>
                </tr>
                
            </tbody>            
        </table>
    </div>

    <!-- 글쓰기 버튼 -->
    <a class="btn btn-primary mt-3" th:href="@{/board_write}">글쓰기</a>

    <!-- 페이지네이션 -->
    <nav aria-label="Page navigation" class="mt-4">
        <ul class="pagination justify-content-center">
            <!-- 이전 페이지 버튼 -->
            <li class="page-item" th:classappend="${currentPage == 0} ? 'disabled'">
                <a class="page-link" 
                   th:href="@{/board_list(page=${currentPage > 0 ? currentPage - 1 : 0}, keyword=${keyword})}" 
                   aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>

            <!-- 페이지 번호 -->
            <li class="page-item" th:each="i : ${#numbers.sequence(0, totalPages - 1)}"
                th:classappend="${i == currentPage} ? 'active'">
                <a class="page-link" th:href="@{/board_list(page=${i}, keyword=${keyword})}" th:text="${i + 1}"></a>
            </li>

            <!-- 다음 페이지 버튼 -->
            <li class="page-item" th:classappend="${currentPage + 1 >= totalPages} ? 'disabled'">
                <a class="page-link" 
                   th:href="@{/board_list(page=${currentPage + 1 < totalPages ? currentPage + 1 : currentPage}, keyword=${keyword})}" 
                   aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
</body>
</html>
````

#### board_update(edit을 update로 바꿈)
````
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판(NEW)</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1>블로그 게시판(NEW)</h1>
        <form th:action="@{/api/board_update/{id}(id=${board.id})}" method="post">
            <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" class="form-control" th:value="${board.title}">
            </div>
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" class="form-control" rows="10" th:text="${board.content}"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">수정</button>
        </form>
        
    </div>
</body>
</html>
````
#### board_view.html
````<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h1 class="mt-5">블로그 게시판</h1>

  
   
    <!-- 게시글 리스트 -->
    <div class="mt-5">
        <h2>게시글 목록</h2>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>글내용</th>
            </tr>
            </thead>
            <tbody>
                <tr th:each="board : ${boards}">
                    <td th:text="${board.content}"></td>
                </tr>

                <tr th:each="board : ${boards}">
                    <td>
                    <!-- 수정 버튼-->
                    <a class="btn btn-warning" th:href="@{/board_edit/{id}(id=${board.id})}">수정</a>
                    <!-- 삭제 버튼-->
                    <form th:action="@{/api/board_delete/{id}(id=${board.id})}" method="post" style="display:inline;">
                    <input type="hidden" name="_method" value="delete">
                    <button type="submit" class="btn btn-danger">삭제</button>
                    </form>
                    </td>
                    </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
````
#### board_write
````
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판(new)</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h1 class="mt-5">블로그 게시판(new)</h1>

    <!--게시글추가폼-->
 <div class="mt-4">
    <h2>게시글추가</h2>
    <form th:action="@{/api/boards}" method="post" name="content">
    <div class="form-group">
    <label for="title">제목:</label>
    <input type="text" class="form-control" id="title" name="title" required>
    </div>
    <div class="form-group">
    <label for="content">내용:</label>
    <textarea class="form-control" id="content" name="content" rows="5" required></textarea>
    </div>
    <button type="submit" class="btn btn-primary">글저장하기</button>
   
    </form>
    <input type="hidden" id="user" name="user" value="GUEST">
    <input type="hidden" id="newdate" name="newdate" value="오늘날짜">
    <input type="hidden" id="count" name="count" value="0">
    <input type="hidden" id="likec" name="likec" value="0">
    </form>
    </div>

    <!-- 게시글 리스트 -->
    <div class="mt-5">
        <h2>게시글 목록</h2>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>글번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>조회수</th>
                    <th>좋아요</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="board : ${boards}">
                    <td th:text="${board.id}"></td>
                    <td>
                        <a th:href="@{/board_view/{id}(id=${board.id})}">
                            <span th:text="${board.title}"></span>
                        </a>
                    </td>
                    <td th:text="${board.user}"></td>
                    <td th:text="${board.newdate}"></td>
                    <td th:text="${board.count}"></td>
                    <td th:text="${board.likec}"></td>
                    <td>
                        <!-- 수정 버튼 -->
                        <a class="btn btn-warning" th:href="@{/board_edit/{id}(id=${board.id})}">수정</a>

                        <!-- 삭제 버튼 -->
                        <form th:action="@{/api/article_delete/{id}(id=${board.id})}" method="post" style="display:inline;">
                           <input type="hidden" name="_method" value="delete">
                           <button type="submit" class="btn btn-danger">삭제</button>
                           </form>

                          
                    </td>
        </table>
    </div>
    <!-- 글쓰기 버튼-->
    <a class="btn btn-warning" th:href="@{/board_write}">글쓰기</a>
                            
</div>
</body>
</html>
````
#### join_end.html
````<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판(new)</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container text-center mt-5">
    <!-- 회원가입 완료 메세지 -->
    <h1 class="mt-4"text-success>회원가입이 완료되었습니다!</h1>
    <p class="mb-4">환영합니다! 이제 블로그 서비스를 자유롭게 이용하실 수 있습니다.</p>
 <!-- 메인 페이지로 돌아가기 버튼-->
 <a href="/" class="btn btn-primary btn-lg">
메인 페이지로 이동
</a>
 </div>
 </body>
````
#### join_new.html
````
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>블로그 게시판(new)</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h1 class="mt-5">회원 가입 화면</h1>

    <!-- 회원가입 폼 -->
    <div class="mt-4">
        <div class="card shadow-sm">
        <div class="card-body">
        <h3 class="card-title text-center mb-4">회원정보입력</h3>
        <form action="@{/api/members}" method="post">
        <!--이름-->
        <div class="form-group">
        <label for="name">이름</label>
        <input type="text" class="form-control" id="name" name="name" placeholder="이름을입력하세요"required>
        </div>
        <!--이메일-->
        <div class="form-group">
        <label for="email">이메일</label>
        <input type="email" class="form-control" id="email" name="email" placeholder="example@email.com"required>
        </div>
         <!--비밀번호-->
        <div class="form-group">
        <label for="password">비밀번호</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를입력하세요"required>
        </div>
        <!--나이-->
        <div class="form-group">
        <label for="age">나이</label>
        <input type="number" class="form-control" id="age" name="age" placeholder="나이를입력하세요"required>
        </div>
        <!--전화번호-->
        <div class="form-group">
        <label for="mobile">전화번호</label>
        <input type="tel" class="form-control" id="mobile" name="mobile" placeholder="010-1234-5678" required>
        </div>
        <!--주소-->
        <div class="form-group">
        <label for="address">주소</label>
        <input type="text" class="form-control" id="address" name="address" placeholder="주소를입력하세요"required>
        </div>
        <!--회원가입버튼-->
        <div class="text-center mt-4">
        <button type="submit" class="btnbtn-primary btn-block">회원가입하기</button>
        </div>
        </form>
        </div>
        </div>
        </div>
        </div>
        </div>
````
#### upload_end.html
````<!DOCTYPE html>
 <html xmlns:th="http://www.thymeleaf.org">
 <head>
 <meta charset="UTF-8">
 <title>블로그 게시판(new)</title>
 <meta content="width=device-width, initial-scale=1.0" name="viewport">
 <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
 </head>
 <body>
 <div class="container text-center mt-5">
 <h1 class="mb-4 text-success">메일 전송(파일 업로드)가 완료되었습니다!</h1>
 <a href="/">홈으로 돌아가기</a>
 </div>
 </body>
 </html>
````














