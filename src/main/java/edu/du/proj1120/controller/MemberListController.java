package edu.du.proj1120.controller;

import edu.du.proj1120.spring.Member;
import edu.du.proj1120.spring.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class MemberListController {

	@Autowired
	private MemberDao memberDao;

	@RequestMapping("/members")
	public String list(
			@ModelAttribute("cmd") ListCommand listCommand,
			Errors errors, Model model) {
		System.out.println("1:"+ listCommand);
		if (errors.hasErrors()) {
			return "member/memberList";
		}
		if (listCommand.getFrom() != null && listCommand.getTo() != null) {
			System.out.println("2:"+listCommand);
			List<Member> members = memberDao.selectByRegdate(
					listCommand.getFrom(), listCommand.getTo());
			model.addAttribute("members", members);
		}
		return "member/memberList";
	}

}