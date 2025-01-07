package edu.du.proj1120.repository;

import edu.du.proj1120.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String Email);
    Optional<Member> findByResetToken(String resetToken);
}
