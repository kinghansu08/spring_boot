package com.example.demo.model.service;
import com.example.demo.model.domain.Member;

public class AddMemberRequest {
        private String name;
        private String email;
        private String password;
        private String age;
        private String mobile;
        private String address;

        public Member toEntity(){ // Member 생성자를 통해 객체 생성
            return Member.builder()
             .name(name)
             .email(email)
             .password(password)
             .age(age)
             .mobile(mobile)
             .address(address)
             .build();
        }
    }
