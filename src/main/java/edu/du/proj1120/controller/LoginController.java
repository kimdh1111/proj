package edu.du.proj1120.controller;

import edu.du.proj1120.spring.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;

@Controller
@RequestMapping("/login") // '/login' 경로에 해당하는 요청을 처리하는 컨트롤러
public class LoginController {

    @Autowired
    private AuthService authService; // 인증 서비스 객체를 주입

    // GET 요청 처리 메서드: 로그인 폼을 반환
    @GetMapping
    public String form(LoginCommand loginCommand, @CookieValue(value = "REMEMBER", required = false) Cookie rCookie) {
        System.out.println("-----------------여기"); // 디버깅용 출력문

        // 'REMEMBER' 쿠키가 있을 경우 이메일 값을 로그인 폼에 미리 채워넣기
        if (rCookie != null) {
            loginCommand.setEmail(rCookie.getValue()); // 쿠키 값(이메일)을 로그인 커맨드 객체에 설정
            loginCommand.setRememberEmail(true); // '이메일 기억하기' 체크박스를 체크 상태로 설정
        }

        // 로그인 폼 화면을 반환
        return "login/loginForm";
    }


    // POST 요청 처리 메서드: 로그인 정보 처리 후 결과를 반환
//    @PostMapping
//    public String submit(LoginCommand loginCommand, Errors errors, HttpSession session, HttpServletResponse response) {
//
//        System.out.println(loginCommand);
//        // LoginCommand 객체의 유효성 검증 (사용자 정의 검증 로직)
//        new LoginCommandValidator().validate(loginCommand, errors);
//
//        // 검증에 오류가 있을 경우, 다시 로그인 폼으로 이동
//        if (errors.hasErrors()) {
//            return "login/loginForm";
//        }
//
//        try {
//            // 인증 서비스에 로그인 시도 (아이디와 비밀번호로 인증)
//            AuthInfo authInfo = authService.authenticate(
//                    loginCommand.getEmail(), // 이메일
//                    loginCommand.getPassword() // 비밀번호
//            );
//
//            // 인증 성공 시, 세션에 사용자 정보를 저장
//            session.setAttribute("authInfo", authInfo);
//
//            // 'REMEMBER' 쿠키 생성 (이메일 저장)
//            Cookie rememberCookie = new Cookie("REMEMBER", loginCommand.getEmail());
//            rememberCookie.setPath("/"); // 쿠키의 유효 범위는 전체 도메인
//
//            // 이메일 기억하기 체크박스가 체크되었으면, 쿠키의 유효 기간을 30일로 설정
//            if (loginCommand.isRememberEmail()) {
//                rememberCookie.setMaxAge(60 * 60 * 24 * 30); // 30일
//            } else {
//                // 체크되지 않으면, 쿠키를 삭제하는 효과를 내기 위해 MaxAge를 0으로 설정
//                rememberCookie.setMaxAge(0); // 쿠키 삭제
//            }
//
//            // 쿠키를 응답에 추가하여 클라이언트에 전달
//            response.addCookie(rememberCookie);
//
//            // 디버깅: 인증된 사용자 이름 출력
//            System.out.println(authInfo.getName() + " 세션 저장!");
//
//            // 로그인 성공 화면 반환
//            return "login/loginSuccess";
//        } catch (WrongIdPasswordException e) {
//            // 인증 실패 시: 오류 메시지 처리 및 로그인 폼으로 이동
//            errors.reject("idPasswordNotMatching"); // 오류 메시지 추가
//            return "login/loginForm"; // 로그인 폼으로 다시 이동
//        }
//    }
}
