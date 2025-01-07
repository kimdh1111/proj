package edu.du.proj1120.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class MainController {


    // /view/main URL로 들어왔을 때
//    @GetMapping("/view/main")
//    public String main(Model model) {
//        // src/main/resources/templates/main.html 파일을 반환
//        return "main";  // 템플릿 경로: src/main/resources/templates/main.html
//    }

    // /view/admin URL로 들어왔을 때
    @GetMapping("/view/admin")
    public String admin() {
        // src/main/resources/templates/admin.html 파일을 반환
        return "admin";  // 템플릿 경로: src/main/resources/templates/admin.html
    }




//
//    // /main URL로 들어왔을 때
//    @GetMapping("/main")
//    public String main2() {
//        // src/main/resources/templates/login/main.html 파일을 반환
//        return "login/main";  // 템플릿 경로: src/main/resources/templates/login/main.html
//    }


}
