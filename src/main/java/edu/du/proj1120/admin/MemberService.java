package edu.du.proj1120.admin;

import edu.du.proj1120.entity.Member;
import edu.du.proj1120.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService
{

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private HttpServletRequest request;


    // 비밀번호 암호화를 위한 BCryptPasswordEncoder
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 관리자의 권한을 확인하는 메서드
    public boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    // 모든 회원 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll(); // MemberRepository의 findAll() 메서드를 사용하여 모든 회원 조회
    }

    // 회원 ID로 조회
    public Member getMemberById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.orElse(null); // 회원이 존재하지 않으면 null을 반환
    }


    public Member updateMember(Long memberId, String email, String name, String password, String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!hasAdminRole(authentication)) {
            throw new IllegalArgumentException("관리자만 회원 정보를 수정할 수 있습니다.");
        }

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.setEmail(email);
            member.setName(name);

            // 비밀번호가 수정된 경우에만 암호화하여 저장
            if (password != null && !password.isEmpty()) {
                String encodedPassword = passwordEncoder.encode(password); // 비밀번호 암호화
                member.setPassword(encodedPassword);  // 암호화된 비밀번호 저장
            } else {
                // 비밀번호가 전달되지 않으면 기존 비밀번호 그대로 두기
                member.setPassword(member.getPassword());  // 원래 비밀번호 유지
            }


            member.setRole(role); // 권한 설정
            // 권한 변경 후 인증 객체 갱신
            updateAuthentication(member);
            // 권한이 변경되면 세션을 초기화 후 새로 인증을 요구
            SecurityContextHolder.clearContext(); // 세션 초기화
            return memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        }
    }


    // 인증 객체 갱신 메서드
    private void updateAuthentication(Member member) {
        // 새로운 인증 객체 생성 (회원의 새로운 권한을 포함)
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                member.getEmail(),  // 사용자의 이메일
                member.getPassword(), // 사용자의 비밀번호
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole())) // 권한에 "ROLE_" prefix 추가
        );

        // 새 인증 객체로 SecurityContext 갱신
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    // 회원 삭제
    public void deleteMember(Long memberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!hasAdminRole(authentication)) {
            throw new IllegalArgumentException("관리자만 회원을 삭제할 수 있습니다.");
        }

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isPresent()) {
            memberRepository.delete(memberOptional.get());
        } else {
            throw new IllegalArgumentException("삭제할 회원을 찾을 수 없습니다.");
        }
    }

}