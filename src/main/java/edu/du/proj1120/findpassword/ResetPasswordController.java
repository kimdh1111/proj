package edu.du.proj1120.findpassword;

import edu.du.proj1120.findpassword.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class ResetPasswordController {

    @Autowired
    private PasswordResetService passwordResetService;



    // 비밀번호 리셋 페이지
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "/find-password/reset-password"; // 비밀번호 리셋 페이지
    }

    // 비밀번호 리셋 처리
    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("newPassword") String newPassword, Model model) {
        try {
            passwordResetService.resetPassword(token, newPassword);
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "message";
    }
}