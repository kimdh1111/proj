package edu.du.proj1120.findpassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ForgotPasswordController {
    @Autowired
    private PasswordResetService passwordResetService;

    // 비밀번호 찾기 페이지 (GET 요청)
    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public String showForgotPasswordPage() {
        return "/find-password/find-password"; // find-password.html 페이지로 이동
    }

    // 비밀번호 찾기 요청 처리 (POST 요청)
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        // 비밀번호 재설정을 위한 토큰 생성 (예: UUID)
        String resetToken = generateResetToken();

        // 이메일 전송
        passwordResetService.sendPasswordResetEmail(email, resetToken);

        model.addAttribute("message", "비밀번호 재설정 이메일을 보냈습니다.");
        return "message"; // 메시지 페이지로 이동
    }

    // 임시로 UUID를 사용하여 토큰을 생성 (실제로는 DB에 저장하여 사용)
    private String generateResetToken() {
        return java.util.UUID.randomUUID().toString();
    }
}
