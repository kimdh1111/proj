package edu.du.proj1120.controller;

import edu.du.proj1120.entity.Member;
import edu.du.proj1120.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class BeginController {

    @Autowired
    MemberRepository memberRepository;

//    @GetMapping("/")
//    public String index() {
//        return "main";
//    }

    @PostConstruct
    public void init(){
        Member member = Member.builder()
                .id(1001L)
                .name("hong1")
                .password(passwordEncoder().encode("1234"))
                .email("hong1@korea.com")
                .role("ADMIN")
                .build();
        memberRepository.save(member);

    }
    private PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
}
