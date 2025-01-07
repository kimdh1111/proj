package edu.du.proj1120.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withUsername("user123")
//                .password(passwordEncoder().encode("1234"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("---------filterChain-------------");





        http.authorizeHttpRequests()
                // 첫 페이지와 메인 페이지를 로그인 없이 접근할 수 있도록 설정
                .antMatchers("/",  "/main").permitAll()  // 첫 페이지와 메인 페이지는 누구나 접근 가능
                .antMatchers("/login/**", "/logout/**", "/register/**", "/css/**", "/js/**", "/images/**", "/board/**", "/admin/**", "/forgot-password", "/reset-password").permitAll()  // 로그인, 회원가입, 정적 리소스는 누구나 접근 가능



                .antMatchers("/**").denyAll()
//                .antMatchers("/sample/admin").hasRole("ADMIN")
                .anyRequest().authenticated();


        http.formLogin()
                .loginPage("/login")  // 기존 로그인 페이지 URL
                .loginProcessingUrl("/login")  // 로그인 폼에서 제출할 URL
                .defaultSuccessUrl("/main", true)  // 로그인 성공 후 리다이렉트될 페이지
                .failureUrl("/login?error=true")  // 로그인 실패 후 리다이렉트될 페이지
                .permitAll();
//        http.csrf().disable();
        http.csrf()
                // CSRF 보호 비활성화
                .ignoringAntMatchers("/logout")  // 로그아웃 URL은 CSRF 보호 예외 처리
//                .ignoringAntMatchers("/board/insertBoard.do") // 파일 업로드 경로에 대해서는 CSRF 보호 무시
//                .ignoringAntMatchers("/register/**")  // 회원가입 경로는 CSRF 보호를 무시
                .and()
                .formLogin();
        http.rememberMe()
                .key("uniqueAndSecret")  // remember-me 쿠키를 생성하는 데 사용되는 키, 보안을 위해 고유한 값 설정
                .tokenValiditySeconds(86400)  // remember-me 쿠키 유효 기간 설정 (예: 1일 = 86400초)
                .rememberMeParameter("remember-me"); // remember-me 체크박스의 이름

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) // POST 방식 로그아웃 처리
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 쿠키 삭제
                .logoutSuccessUrl("/main"); // 로그아웃 후 리다이렉트 URL

        http.exceptionHandling().accessDeniedPage("/sample/accessDenied");
        http.csrf()
                .ignoringAntMatchers("/h2-console/**")
                .and().headers().frameOptions().sameOrigin();
        return http.build();
    }


}