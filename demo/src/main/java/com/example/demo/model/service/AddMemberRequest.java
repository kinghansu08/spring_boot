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
