package edu.du.proj1120.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberRegisterService {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private PasswordEncoder passwordEncoder;  // PasswordEncoder 주입

	public Long regist(RegisterRequest req) {
		// 이메일로 회원이 이미 존재하는지 확인
		Member member = memberDao.selectByEmail(req.getEmail());
		if (member != null) {
			throw new DuplicateMemberException("dup email " + req.getEmail());
		}

		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(req.getPassword());  // 비밀번호 암호화

		// 새로운 회원 객체 생성 (암호화된 비밀번호 사용)
		Member newMember = new Member(
				req.getEmail(), encodedPassword, req.getName(),
				LocalDateTime.now());

		// 회원을 데이터베이스에 저장
		memberDao.insert(newMember);

		// 저장된 회원의 ID 반환
		return newMember.getId();
	}
}
