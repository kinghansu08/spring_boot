package com.example.demo.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
//추가
import java.nio.file.Path;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//추가
import org.springframework.beans.factory.annotation.Value;


public class FileController {
    @Value("${spring.servlet.multipart.location}") // properties 등록된 설정(경로) 주입
private String uploadFolder;

 @PostMapping("/upload-email")
 public String uploadEmail( // 이메일, 제목, 메시지를 전달받음
 @RequestParam("email") String email,
 @RequestParam("subject") String subject,
 @RequestParam("message") String message,
 RedirectAttributes redirectAttributes) {

    try{
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
   