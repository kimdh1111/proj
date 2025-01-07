package edu.du.proj1120.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String userEmail) {
        String token = UUID.randomUUID().toString();  // 비밀번호 재설정 토큰 생성
        String resetLink = "http://localhost:8080/reset-password?token=" + token;  // 재설정 링크

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("비밀번호 재설정 요청");
        message.setText("비밀번호를 재설정하려면 아래 링크를 클릭하세요:\n" + resetLink);

        mailSender.send(message);

        // TODO: 생성된 토큰을 저장하는 코드 (DB 또는 메모리 사용)
    }
}
