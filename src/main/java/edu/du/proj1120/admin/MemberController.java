package edu.du.proj1120.admin;

import edu.du.proj1120.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")  // 관리자 관련 URL
public class MemberController {
    @Autowired
    private MemberService memberService;


    // 회원 목록 보기
    @GetMapping("/members")
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "admin/memberList";  // 회원 목록 페이지
    }

    // 회원 수정 폼 보기
    @GetMapping("/editMember/{id}")
    public String showEditMemberForm(@PathVariable Long id, Model model) {
        Member member = memberService.getMemberById(id);
        if (member != null) {
            model.addAttribute("member", member);
            return "admin/editMember";  // 회원 수정 폼 페이지
        }

        return "redirect:/admin/members";  // 회원이 없으면 목록 페이지로 리다이렉트
    }

    // 회원 수정 처리
    @PostMapping("/editMember/{id}")
    public String updateMember(@PathVariable Long id,
                               @RequestParam String email,
                               @RequestParam String name,
                               @RequestParam String password,
                               @RequestParam String role) {
        memberService.updateMember(id, email, name, password, role);

        return "redirect:/admin/members";  // 수정 후 회원 목록 페이지로 리다이렉트
    }

    // 회원 삭제 처리
    @GetMapping("/deleteMember/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";  // 삭제 후 회원 목록 페이지로 리다이렉트
    }
}
