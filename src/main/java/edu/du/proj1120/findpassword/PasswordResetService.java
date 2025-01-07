package edu.du.proj1120.findpassword;

import edu.du.proj1120.entity.Member;
import edu.du.proj1120.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public void initiatePasswordReset(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: " + email));

        // 비밀번호 리셋 토큰 생성 및 만료 시간 설정
        String resetToken = UUID.randomUUID().toString();
        member.setResetToken(resetToken);
        member.setResetTokenExpiryDate(LocalDateTime.now().plusHours(1)); // 1시간 유효

        memberRepository.save(member);

        // 리셋 링크 이메일 전송
        sendPasswordResetEmail(member.getEmail(), resetToken);
    }

    // 비밀번호 리셋 이메일 전송
    public void sendPasswordResetEmail(String email, String resetToken) {
        // 이메일 전송 준비
        String resetLink = "https://yourdomain.com/reset-password?token=" + resetToken;
        String subject = "비밀번호 재설정 요청";
        String text = "안녕하세요, \n\n비밀번호 재설정을 위해 아래 링크를 클릭하세요:\n" + resetLink;

        // 이메일 메시지 생성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@example.com"); // 보내는 사람 이메일
        message.setTo(email); // 받는 사람 이메일
        message.setSubject(subject); // 이메일 제목
        message.setText(text); // 이메일 본문

        // 이메일 전송
        mailSender.send(message);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        Member member = memberRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        // 토큰 만료 여부 체크
        if (member.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        // 새 비밀번호로 업데이트 후 토큰 제거
        member.setPassword(newPassword);
        member.setResetToken(null);
        member.setResetTokenExpiryDate(null);

        memberRepository.save(member);
    }
}
