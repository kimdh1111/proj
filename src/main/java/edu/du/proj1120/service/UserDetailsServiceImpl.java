package edu.du.proj1120.service;

import edu.du.proj1120.entity.Member;
import edu.du.proj1120.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("==========>사용자: " + username);
//        UserDetails user = User.withUsername("user123")
//                .password(passwordEncoder().encode("1234"))
//                .roles("ADMIN")
//                .build();
//        return user;
//
        Optional<Member> member = memberRepository.findByEmail(username);
                if (!member.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return toUserDetails(member.get());
    }
    private PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    private UserDetails toUserDetails(Member member){
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
//                .authorities(new SimpleGrantedAuthority(member.getRole().toString()))
                .roles(member.getRole())
                .build();
    }
}