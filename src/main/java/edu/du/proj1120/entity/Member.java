package edu.du.proj1120.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data            // Getter Setter
@Builder        // DTO -> Entity화
@AllArgsConstructor    // 모든 컬럼 생성자 생성
@NoArgsConstructor    // 기본 생성자
@Table(name = "member")
public class Member {

    @Id    // 내가 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)	// 자동 id 생성
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private String role;

    private LocalDateTime regdate;

    private String resetToken;  // 비밀번호 리셋 토큰
    private LocalDateTime resetTokenExpiryDate;  // 토큰 만료 시간
}