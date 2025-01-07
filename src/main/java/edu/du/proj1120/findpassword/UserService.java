package edu.du.proj1120.findpassword;

import edu.du.proj1120.entity.Member;
import edu.du.proj1120.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordResetService passwordResetService;  // PasswordResetService 주입

    // 유효한 비밀번호 리셋 토큰인지 확인
    public boolean isValidResetToken(String token) {
        // 토큰 검증 로직 (DB에서 토큰을 확인하거나 만료 여부 체크)
        Member member = memberRepository.findByResetToken(token).orElse(null);
        return member != null && member.getResetTokenExpiryDate().isAfter(LocalDateTime.now());
    }

    // 비밀번호 리셋
    public boolean resetPassword(String token, String newPassword) {
        // 토큰으로 회원 찾기
        Member member = memberRepository.findByResetToken(token).orElse(null);
        if (member == null) {
            return false; // 회원이 없으면 실패
        }

        // 비밀번호 암호화 (BCrypt 암호화 사용)
        String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);

        // 비밀번호 변경
        member.setPassword(encryptedPassword);
        member.setResetToken(null);  // 리셋 후 토큰 비우기
        member.setResetTokenExpiryDate(null);  // 리셋 후 만료 시간 비우기

        // 변경된 회원 저장
        memberRepository.save(member);

        return true;
    }
}
